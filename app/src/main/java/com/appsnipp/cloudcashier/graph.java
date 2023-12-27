import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.appsnipp.cloudcashier.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class graph extends AppCompatActivity {

    private LineChart expenseLineChart;
    private DatabaseReference expensesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_chart);

        // Initialize the LineChart
        expenseLineChart = findViewById(R.id.expenseLineChart);
        expensesRef = FirebaseDatabase.getInstance().getReference().child("expenses");

        // Fetch data from Firebase Database and update the chart
        fetchExpenseData();
    }

    private void fetchExpenseData() {
        expensesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Entry> entries = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Assuming the expense amount is stored as a field in the 'expenses' node
                    Double expenseAmount = dataSnapshot.child("amount").getValue(Double.class);

                    // Assuming the timestamp is stored as a field in the 'expenses' node
                    Long timestamp = dataSnapshot.child("timestamp").getValue(Long.class);

                    entries.add(new Entry(timestamp, expenseAmount.floatValue()));
                }

                // Set up the LineChart with the fetched data
                setupLineChart(entries);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }

    private void setupLineChart(List<Entry> entries) {
        LineDataSet dataSet = new LineDataSet(entries, "Expense Spending");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);

        LineData lineData = new LineData(dataSet);

        // Format x-axis as timestamp (you may need to adjust the format based on your data)
        XAxis xAxis = expenseLineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                // Format the timestamp as needed
                // For example, you can use SimpleDateFormat to format the date
                return String.valueOf((long) value);
            }
        });

        expenseLineChart.setData(lineData);
        expenseLineChart.getDescription().setText("Expense Spending Over Time");
        expenseLineChart.invalidate(); // refresh chart
    }
}
