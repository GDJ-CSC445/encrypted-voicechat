module edu.oswego.cs.encryptedvoicechat {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens edu.oswego.cs.encryptedvoicechat to javafx.fxml;
    exports edu.oswego.cs.encryptedvoicechat;
}