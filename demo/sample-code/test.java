public class test {

    // ===== 1. 命令注入 =====
    public void deleteFile(String filename) {
        try {
            Runtime.getRuntime().exec("rm -rf " + filename);
        } catch (Exception e) {}
    }

    // ===== 2. 路径遍历 =====
    public void readFile(String filename) {
        java.io.File file = new java.io.File("/var/data/" + filename);
    }

    // ===== 3. XSS =====
    public String searchUser(String keyword) {
        return "<div>搜索结果：" + keyword + "</div>";
    }

    // ===== 4. XXE =====
    public void parseXML(String xmlData) {
        try {
            javax.xml.parsers.DocumentBuilderFactory dbf = 
                javax.xml.parsers.DocumentBuilderFactory.newInstance();
            dbf.newDocumentBuilder().parse(new org.xml.sax.InputSource(
                new java.io.StringReader(xmlData)
            ));
        } catch (Exception e) {}
    }

    // ===== 5. 不安全反序列化 =====
    public void deserialize(byte[] data) {
        try {
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(data);
            java.io.ObjectInputStream ois = new java.io.ObjectInputStream(bis);
            Object obj = ois.readObject();
        } catch (Exception e) {}
    }

    // ===== 6. SSRF =====
    public void fetchURL(String url) {
        try {
            java.net.HttpURLConnection conn = 
                (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
            conn.connect();
        } catch (Exception e) {}
    }

    // ===== 7. 弱加密 =====
    public void encryptData(String data) {
        try {
            java.security.MessageDigest md = 
                java.security.MessageDigest.getInstance("MD5");
            md.digest(data.getBytes());
        } catch (Exception e) {}
    }

    // ===== 8. 弱随机数 =====
    public String generateToken() {
        java.util.Random rand = new java.util.Random();
        int token = rand.nextInt(1000000);
        return String.valueOf(token);
    }

    // ===== 9. 信息泄露 =====
    public void processFile(String path) {
        try {
            java.io.File f = new java.io.File(path);
        } catch (Exception e) {
            System.out.println("文件 " + path + " 不存在");
        }
    }

    // ===== 10. 文件上传漏洞 =====
    public void uploadFile(String filename, byte[] content) {
        try {
            java.io.FileOutputStream fos = 
                new java.io.FileOutputStream("/uploads/" + filename);
            fos.write(content);
            fos.close();
        } catch (Exception e) {}
    }

    // ===== 11. SQL注入 =====
    public void searchOrder(String orderId) {
        String query = "SELECT * FROM orders WHERE id = " + orderId;
    }

    // ===== 12. 硬编码密钥 =====
    private static final String AUTH_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    private static final String DB_PASSWORD = "root123";

    // ===== 13. 日志注入 =====
    public void logUserAction(String action) {
        System.out.println("用户操作：" + action);
    }

    public static void main(String[] args) {
        test t = new test();
        t.searchUser("<script>alert(1)</script>");
        System.out.println("test 运行完成");
    }
}