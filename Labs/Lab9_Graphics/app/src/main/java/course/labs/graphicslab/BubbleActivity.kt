package course.labs.graphicslab

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import course.labs.graphicslab.databinding.ActivityBubbleBinding

class BubbleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBubbleBinding

    // Bubble image's bitmap
    private lateinit var mBitmap: Bitmap
    private lateinit var bubbleView: BubbleView
    private lateinit var scaledBitmap: Bitmap
    private lateinit var positionViewModel: BubblePositionViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBubbleBinding.inflate(layoutInflater)
        setContentView(binding.frame)

        setupUI()

        // ToDo:
        positionViewModel = ViewModelProvider(this)[BubblePositionViewModel::class.java]
        positionViewModel.bindToActivityLifecycle(this)
        // 1. Get ViewModel (BubblePositionViewModel) using ViewModelProvider
        // 2. Tie BubblePositionViewModel to BubbleActivity lifecycle

    }

    private fun setupUI() {
        // Set up user interface

        // ToDo:
        mBitmap = BitmapFactory.decodeResource(resources,R.drawable.shiny_steel_ball)
        scaledBitmap = Bitmap.createScaledBitmap(mBitmap, BITMAP_SIZE, BITMAP_SIZE, false)
        bubbleView = BubbleView(applicationContext, (-1).toFloat(),(-1).toFloat())
        // 1. Load basic bubble Bitmap using resources and R.drawable.shiny_steel_ball
        // 2. Scale bitmap to BITMAP_SIZE
        // 3. Create BubbleView. Position offscreen initially
    }

    // Start animation here because you know the Activity
    // is visible on the screen
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            beginObservingCounts()
            with(binding.frame) {
                // ToDo:
                positionViewModel.startMotion(resources.displayMetrics.widthPixels,resources.displayMetrics.heightPixels, BITMAP_SIZE)
                // Start animation motion here using the positionViewModel
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // ToDo:
        binding.frame.addView(bubbleView)
        // Add BubbleView to FrameLayout
    }

    override fun onStop() {
        // ToDo:
        binding.frame.removeView(bubbleView)
        // Remove BubbleView from FrameLayout
        super.onStop()
    }

    private fun beginObservingCounts() {
        // Register observers of image position

        // ToDo:
        positionViewModel.location.observe(this) {
            bubbleView.x = positionViewModel.location.value!!.first
            bubbleView.y = positionViewModel.location.value!!.second
            bubbleView.invalidate()
        }
        // 1. Get the position from ViewModel
        // 2. Set the x and y attributes of the bubble view to the position values retrieved in the previous step
        // 3. invalidate bubble view
    }

// BubbleView is a View that displays a bubble.
// This class handles drawing and animating the BubbleView
    inner class BubbleView internal constructor(context: Context, x: Float, y: Float) :
        View(context) {
        private val painter = Paint()

        init {
            // Creates the bubble bitmap for this BubbleView
            this.x = x
            this.y = y
            painter.isAntiAlias = true
        }

        // Draw the Bubble at its current location
        override fun onDraw(canvas: Canvas) {
            canvas.drawBitmap(scaledBitmap, 0f, 0f, painter)
        }
    }

    companion object {
        private const val BITMAP_SIZE = 250
    }
}