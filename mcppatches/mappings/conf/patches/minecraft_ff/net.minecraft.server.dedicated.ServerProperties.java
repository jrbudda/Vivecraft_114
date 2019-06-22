diff -r -U 3 minecraft\net\minecraft\server\dedicated\ServerProperties.java minecraft_patched\net\minecraft\server\dedicated\ServerProperties.java
--- minecraft\net\minecraft\server\dedicated\ServerProperties.java
+++ minecraft_patched\net\minecraft\server\dedicated\ServerProperties.java
@@ -56,6 +56,6 @@
    public final int field_219004_Q;
-   public final PropertyManager<ServerProperties>.a<Integer> field_219005_R;
+   public final Property field_219005_R;
-   public final PropertyManager<ServerProperties>.a<Boolean> field_219006_S;
+   public final Property field_219006_S;

    public ServerProperties(Properties p_i1466_1_) {
       super(p_i1466_1_);
