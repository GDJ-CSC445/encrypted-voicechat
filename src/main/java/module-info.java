module edu.oswego.cs.encryptedvoicechat {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    exports edu.oswego.cs;
    opens edu.oswego.cs to javafx.fxml;
}