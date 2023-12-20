package com.emadomarah.food

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    var breakfastTile: TextView? = null
    var breakfastDesc:TextView? = null
    var lunchTitle:TextView? = null
    var lunchDesc:TextView? = null
    var dinnerTitle:TextView? = null
    var dinnerDesc:TextView? = null
    var breakfastImg: ImageView? = null
    var lunchImg:android.widget.ImageView? = null
    var dinnerImg:android.widget.ImageView? = null
    var preferences: SharedPreferences? = null
    var bmi: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= 21) {
            window.navigationBarColor = ContextCompat.getColor(
                this,
                R.color.homestatebar
            ) // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            window.statusBarColor = ContextCompat.getColor(
                this,
                R.color.statusbarrecom
            ) //status bar or the time bar at the top
        }

        preferences = getSharedPreferences("Calories", MODE_PRIVATE)
        bmi = preferences!!.getLong("bmr", 0)
        initviews()

        if (bmi < 1200) {
            breakfastTile!!.text = "Breakfast (287 calories)"
            breakfastDesc?.setText("Combine 3/4 cup bran flakes, 1 banana, and 1 cup fat-free milk in a bowl.")
            breakfastImg!!.setImageResource(R.drawable.b1)
            lunchTitle?.setText("Breakfast (287 calories)")
            lunchDesc?.setText(
                "Blend 1 cup frozen berries, 1/2 banana, and 8 ounces of low- or fat-free milk into a smoothie.\n" +
                        "Grab 1 or 2 hard-boiled eggs on your way out the door"
            )
            lunchImg?.setImageResource(R.drawable.b4)
            dinnerTitle?.setText("Breakfast (258 calories)")
            dinnerDesc?.setText(
                "In the microwave, cook 1/2 cup quick-cooking oats with low-fat or unsweetened soy milk. \n" +
                        "Add 1/2 apple (sliced or chopped), 1 teaspoon honey, and a pinch of cinnamon."
            )
            dinnerImg?.setImageResource(R.drawable.b7)
        } else if (bmi > 1200 && bmi < 2000) {
            breakfastTile!!.text = "Lunch (360 calories)"
            breakfastDesc?.setText(
                "Heat up 1 cup vegetarian vegetable soup and serve with 1 veggie burger in a 100% whole-grain toast,\n" +
                        "sandwich thin, or English muffin and 1 cup of grapes."
            )
            breakfastImg!!.setImageResource(R.drawable.b5)
            lunchTitle?.setText("Lunch (360 calories)")
            lunchDesc?.setText(
                "Build a sandwich with 1 mini whole wheat pita, 3 ounces turkey breast, \n" +
                        "1/2 roasted pepper, 1 teaspoon mayo, mustard, and lettuce. \n" +
                        "Serve with 1 stick part-skim mozzarella string cheese and 2 kiwis."
            )
            lunchImg?.setImageResource(R.drawable.b2)
            dinnerTitle?.setText("Lunch (380 calories)")
            dinnerDesc?.setText(
                ("To make a chicken salad, toss 4 ounces shredded skinless roast chicken breast with 1/4 cup sliced red grapes, \n" +
                        "1 tablespoon slivered almonds, 1 tablespoon mayonnaise, and 1 tablespoon plain,\n" +
                        "unsweetened Greek yogurt. Serve over lettuce. Eat with 1 banana")
            )
            dinnerImg?.setImageResource(R.drawable.b8)
        } else {
            breakfastTile!!.text = "Dinner (420 calories)"
            breakfastDesc?.setText(
                ("Brush 4 ounces boneless, skinless chicken breast with barbecue sauce and grill. \n" +
                        "Combine 2 heaping cups of sautéed spinach with garlic, \n" +
                        "olive oil, and tomatoes and serve with 1/2 plain baked or sweet potato (as desired).")
            )
            breakfastImg!!.setImageResource(R.drawable.b6)
            lunchTitle?.setText("Dinner (420 calories)")
            lunchDesc?.setText(
                ("Serve 4 ounces broiled flounder or sole with 2 sliced plum tomatoes \n" +
                        "sprinkled with 2 tablespoons grated Parmesan cheese,broiled until just golden. \n" +
                        "Eat with 1 cup cooked couscous and 1 cup steamed broccoli. Enjoy with a single-serve ice cream")
            )
            lunchImg?.setImageResource(R.drawable.b3)
            dinnerTitle?.setText("Dinner (415 calories)")
            dinnerDesc?.setText(
                "Serve 4 ounces steamed shrimp with 1 baked potato topped with 3 tablespoons salsa and 1 tablespoon unsweetened Greek yogurt, \n" +
                        "plus 3 cups spinach, steamed . Finish the meal off with 1 ounce of chocolate or a 100- to 150-calorie ice cream bar."
            )
            dinnerImg?.setImageResource(R.drawable.b9)
        }


    }


    fun initviews() {
        breakfastTile = findViewById(R.id.bf_title)
        breakfastDesc = findViewById<TextView>(R.id.bf_desc)
        breakfastImg = findViewById(R.id.bf_img)
        lunchTitle = findViewById<TextView>(R.id.lunch_title)
        lunchDesc = findViewById<TextView>(R.id.lunch_desc)
        lunchImg = findViewById<ImageView>(R.id.lunch_img)
        dinnerTitle = findViewById<TextView>(R.id.dinner_title)
        dinnerDesc = findViewById<TextView>(R.id.dinner_desc)
        dinnerImg = findViewById<ImageView>(R.id.dinner_img)
    }


    fun RecomndationOnClick(view: View) {
        when (view.id) {
            R.id.back_btn -> onBackPressed()
        }
    }
}
