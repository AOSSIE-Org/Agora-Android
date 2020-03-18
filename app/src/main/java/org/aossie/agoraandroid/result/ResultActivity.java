package org.aossie.agoraandroid.result;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;
import org.aossie.agoraandroid.R;

public class ResultActivity extends AppCompatActivity {
  private final List<PieEntry> value = new ArrayList<>();
  private String mWinnerName;
  private int numerator, denominator;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_result);
    TextView winnerName = findViewById(R.id.text_view_winner_name);

    if (getIntent().hasExtra("name") && getIntent().hasExtra("numerator") && getIntent().hasExtra(
        "denominator")) {
      mWinnerName = getIntent().getStringExtra("name");
      numerator = Integer.parseInt(getIntent().getStringExtra("numerator"));
      denominator = Integer.parseInt(getIntent().getStringExtra("denominator"));
    }
    PieChart pieChart = findViewById(R.id.pie_chart);
    pieChart.setUsePercentValues(true);

    Description description = new Description();
    description.setText("Election Result in percentage");
    pieChart.setDescription(description);

    value.add(new PieEntry(numerator, mWinnerName));
    value.add(new PieEntry(denominator, "Others"));

    PieDataSet pieDataSet = new PieDataSet(value, "");
    PieData pieData = new PieData(pieDataSet);
    pieChart.setData(pieData);
    pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
    pieChart.animateXY(1400, 1400);
    winnerName.setText(mWinnerName);
  }
}
