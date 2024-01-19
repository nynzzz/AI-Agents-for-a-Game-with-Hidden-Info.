module sneakthrough {
    requires javafx.controls;
    requires com.opencsv;
    requires py4j;
    exports sneakthrough.GUI;
    opens sneakthrough.GUI to javafx.graphics;

}
