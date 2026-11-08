from pathlib import Path
import xml.etree.ElementTree as etree
import re, tempfile, subprocess, textwrap, json, sys
root=Path(__file__).resolve().parents[1]
res=root/'app/src/main/res'
layout=list((res/'layout').glob('*.xml'))
all_xml=list((res).rglob('*.xml'))+[root/'app/src/main/AndroidManifest.xml']
checks=[]
def report(name,ok,detail=''):
 checks.append(dict(check=name,passed=bool(ok),detail=detail)); print(('PASS' if ok else 'FAIL'), name, detail)
for path in all_xml:
 try: etree.parse(str(path))
 except Exception as e: report('XML '+path.name,False,str(e))
report('XML parsing',len([c for c in checks if not c['passed']])==0,f'{len(all_xml)} files')
# resources
names={'string':set(),'color':set(),'drawable':set(p.stem for p in (res/'drawable').glob('*.xml')),'layout':set(p.stem for p in layout),'style':set(),'id':set()}
names['color'].update(p.stem for p in (res/'color').glob('*.xml'))
for xml in (res/'values').glob('*.xml'):
 doc=etree.parse(str(xml))
 for el in doc.getroot():
  if 'name' in el.attrib and el.tag in names:names[el.tag].add(el.attrib['name'])
for xml in all_xml:
 for node in etree.parse(str(xml)).iter():
  for val in node.attrib.values():
   for item in re.findall(r'@\+id/([a-zA-Z0-9_]+)',val):names['id'].add(item)
missing=[]
for f in all_xml:
 data=f.read_text()
 for kind,name in re.findall(r'@(?!(?:android:|\+))(string|color|drawable|layout|style|id)/([a-zA-Z0-9_.]+)',data):
  if name not in names[kind]:missing.append((f.name,kind,name))
report('Android resource references',not missing,f'{len(missing)} missing: {missing[:10]}')
java=list((root/'app/src/main/java').rglob('*.java'))
java_source='\n'.join(f.read_text() for f in java)
missing_java=[]
for kind,name in re.findall(r'R\.(string|id|drawable|layout)\.([A-Za-z_][A-Za-z_0-9]*)',java_source):
 if name not in names[kind]: missing_java.append((kind,name))
report('Java R.* resource references',not missing_java,f'{len(missing_java)} missing {missing_java}')
# Java grammar
parser_java='''import javax.tools.*;import com.sun.source.util.*;import java.nio.file.*;import java.util.*;
public class JavaSyntaxCheck {public static void main(String[] args) throws Exception {
 var compiler=ToolProvider.getSystemJavaCompiler();var diags=new DiagnosticCollector<JavaFileObject>();var mgr=compiler.getStandardFileManager(diags,null,null);var files=mgr.getJavaFileObjectsFromStrings(Arrays.asList(args));var task=(JavacTask)compiler.getTask(null,mgr,diags,Arrays.asList("-proc:none"),null,files);int n=0;for(var tree:task.parse()){n++;};var errors=diags.getDiagnostics().stream().filter(d->d.getKind()==Diagnostic.Kind.ERROR).toList();for(var d:errors){System.err.println(d);}if(!errors.isEmpty())System.exit(1);System.out.println("Parsed Java files: "+n);}}
'''
with tempfile.TemporaryDirectory() as tmp:
 p=Path(tmp)/'JavaSyntaxCheck.java';p.write_text(parser_java)
 r=subprocess.run(['javac',str(p)],capture_output=True,text=True)
 if r.returncode!=0:report('Java parser preparation',False,r.stderr)
 else:
  out=subprocess.run(['java','-cp',tmp,'JavaSyntaxCheck']+[str(p) for p in java],capture_output=True,text=True)
  report('Java syntax parse',out.returncode==0,(out.stdout+out.stderr).strip()[:500])
manifest=(root/'app/src/main/AndroidManifest.xml').read_text()
report('Splash only launcher',manifest.count('android.intent.action.MAIN')==1 and 'name=".SplashActivity"' in manifest)
report('Internet permission', 'android.permission.INTERNET' in manifest)
report('Firebase auth + Firestore implemented', all(x in java_source for x in ('signInWithEmailAndPassword','sendPasswordResetEmail','createUserWithEmailAndPassword','FirebaseFirestore','signOut()')))
report('Firebase session splash routing', all(x in java_source for x in ('getCurrentUser()', 'postDelayed(navigate, 1500)')))
report('Required package name', 'pk.edu.nfciet.shopease' in (root/'app/build.gradle.kts').read_text())
result={'passed':sum(x['passed'] for x in checks),'total':len(checks),'checks':checks}
(root/'source-verification-results.json').write_text(json.dumps(result,indent=2))
print(json.dumps({'passed':result['passed'],'total':result['total']}))
if result['passed']!=result['total']:sys.exit(1)
