package org.gradle.wrapper;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * ShopEase's offline-built Gradle bootstrap. On first use it fetches an official Gradle
 * distribution; subsequent uses run the cached binary. This is not the Gradle-issued JAR.
 * It is provided because the official wrapper JAR could not be fetched in the build sandbox.
 */
public final class GradleWrapperMain {
    private GradleWrapperMain() { }
    public static void main(String[] args) throws Exception {
        Path root = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        Properties prop = new Properties();
        try (InputStream in = Files.newInputStream(root.resolve("gradle/wrapper/gradle-wrapper.properties"))) {
            prop.load(in);
        }
        String address = prop.getProperty("distributionUrl");
        // Reject arbitrary binaries. This project pins the approved Gradle 8.13 binary distribution.
        if (!"https://services.gradle.org/distributions/gradle-8.13-bin.zip".equals(address)) {
            throw new IllegalStateException("Unexpected Gradle distribution URL: " + address);
        }
        Path cache = Paths.get(System.getProperty("user.home"), ".gradle", "wrapper", "dists", "shopease-gradle-8.13");
        Path gradle = cache.resolve("gradle-8.13");
        boolean windows = System.getProperty("os.name").toLowerCase().contains("win");
        Path runner = gradle.resolve(windows ? "bin/gradle.bat" : "bin/gradle");
        if (!Files.isRegularFile(runner)) {
            Files.createDirectories(cache);
            Path zip = cache.resolve("download.zip");
            System.out.println("ShopEase: downloading official Gradle 8.13 (first run only)...");
            HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS)
                    .connectTimeout(Duration.ofSeconds(30)).build();
            HttpRequest request = HttpRequest.newBuilder(URI.create(address))
                    .timeout(Duration.ofMinutes(12)).GET().build();
            HttpResponse<Path> response = client.send(request, HttpResponse.BodyHandlers.ofFile(zip));
            if (response.statusCode() != 200) {
                throw new IllegalStateException("Gradle download failed: HTTP " + response.statusCode());
            }
            try (ZipInputStream zin = new ZipInputStream(Files.newInputStream(zip))) {
                ZipEntry entry;
                while ((entry = zin.getNextEntry()) != null) {
                    Path dst = cache.resolve(entry.getName()).normalize();
                    if (!dst.startsWith(cache)) throw new IllegalStateException("Unsafe archive entry");
                    if (entry.isDirectory()) {
                        Files.createDirectories(dst);
                    } else {
                        Files.createDirectories(dst.getParent());
                        Files.copy(zin, dst, StandardCopyOption.REPLACE_EXISTING);
                    }
                    zin.closeEntry();
                }
            }
            Files.deleteIfExists(zip);
            if (!windows && Files.isRegularFile(runner)) runner.toFile().setExecutable(true);
        }
        if (!Files.isRegularFile(runner)) throw new IllegalStateException("Gradle binary was not extracted");
        List<String> command = new ArrayList<>();
        if (windows) { command.add("cmd"); command.add("/c"); }
        command.add(runner.toString());
        command.addAll(Arrays.asList(args));
        int exit = new ProcessBuilder(command).directory(root.toFile()).inheritIO().start().waitFor();
        System.exit(exit);
    }
}
