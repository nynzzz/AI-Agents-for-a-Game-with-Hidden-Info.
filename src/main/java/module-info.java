module sneakthrough {
    requires javafx.controls;
    requires com.opencsv;
    requires org.json;
    exports sneakthrough.GUI;
    opens sneakthrough.GUI to javafx.graphics;

}
