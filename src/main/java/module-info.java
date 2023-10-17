module sneakthrough {
    requires javafx.controls;
    requires com.opencsv;
    exports sneakthrough.GUI;
    opens sneakthrough.GUI to javafx.graphics;

}
