package application;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Slider;


public class SavingsGraph {
    private String name;

    private SubviewSlider savings;
    private SubviewSlider interest;

    private XYChart.Series<Number, Number> dataSavings;
    private XYChart.Series<Number, Number> dataInterests;

    private LineChart<Number, Number> lineChart;



    public SavingsGraph(String name, SubviewSlider savings, SubviewSlider interest) {
        this.name = name;

        this.savings = savings;
        this.interest = interest;
        
        this.dataSavings = initDataSet("Savings");
        this.dataInterests = initDataSet("Interest");
        
        this.lineChart = makeLineChart();
        

    }


    // intiatite section;  on being made ==========
    private LineChart<Number,Number> makeLineChart(){
        // Create linechart
        NumberAxis xAxis = new NumberAxis(0, 30, 1); // years
        
        //NumberAxis yAxis = new NumberAxis(0, getUpperBound(), 2500); // upperbound adjusts to 'valueSavings' and 'valueinterests'
        NumberAxis yAxis = new NumberAxis(); // If there are no "set" boundaries AND working with dynamic data, this will auto adjust.


        LineChart<Number, Number> lineChart = new LineChart<Number,Number>(xAxis, yAxis);
        //lineChart.setTitle("Savings calculator");// Final rev

        lineChart.setTitle("Savings calculator - " + getYearlySaving() + "-" + getMaxSavings() + "-"  + getMaxInterest());// testing only
        lineChart.setAnimated(false);
        

        // dataSet
        lineChart.getData().add(this.dataSavings);
        lineChart.getData().add(this.dataInterests);




        // events
        this.savings.getSlider().valueProperty().addListener(new ChangeListener<Number>() { // Resource #2

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!savings.getSlider().isValueChanging()){
                    updateLineChart();
                }
            }
        }); 

        this.interest.getSlider().valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!interest.getSlider().isValueChanging()) {
                    updateLineChart();
                }
            }
        });


        return lineChart;

    }

    private XYChart.Series<Number, Number> initDataSet(String name) {
        
        XYChart.Series<Number,Number> dataSetSaving = new XYChart.Series <Number,Number>();
        dataSetSaving.setName(name);

        updateDataSavings();
        updateDataInterests();

        return dataSetSaving;
    }
    

    // update section =========

    public void updateLineChart() {
        this.lineChart.getData().clear();
                    
        updateDataSavings();
        updateDataInterests();
        
        this.lineChart.getData().add(dataSavings);
        this.lineChart.getData().add(dataInterests);
    }

    public void updateDataSavings() {

        try {
            this.dataSavings.getData().clear();
        } catch (Exception e) {
            return;
        }

        Double total = 0.0;
        for (int i = 0; i <= 30; i++) { // 'i' is year
            this.dataSavings.getData().add(new XYChart.Data<Number, Number>(i, total));
            total += getYearlySaving();
        }

    }

    public void updateDataInterests() {
        
        try {
            this.dataInterests.getData().clear();
        } catch (Exception e) {
            return;
        }
        
        Double currentAmount = getYearlySaving();

        for (int i = 0; i <= 30; i++) {

            if (i == 0) {
                this.dataInterests.getData().add(new XYChart.Data<Number,Number>(0, 0));
                currentAmount += getYearlySaving();
                continue;
            }

            this.dataInterests.getData().add(new XYChart.Data<Number,Number>(i, currentAmount));
            currentAmount = (currentAmount + getYearlySaving()) * getInterestAmount();

        }
    }


    // 'get'  section ==========
    public LineChart <Number,Number> getLineChart() {
        this.lineChart = makeLineChart();
        return this.lineChart;
    }

    public Slider getSavingsSlider() {
        return this.savings.getSlider();
    }

    public Slider getInterestSlider() {
        return this.interest.getSlider();
    }

    public Double getYearlySaving() {
        return 12 * this.savings.getValuePosition();
    }
    
    public Double getMaxSavings() {
        Double max = getYearlySaving() * 30;
        
        return max;
    }

    private Double getInterestAmount() {
        
        return 1 + (this.interest.getValuePosition() * .01);
    }

    public Double getMaxInterest() {
        return getMaxSavings() * getInterestAmount();
    }

}


