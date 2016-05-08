# SliderWidget

![Swipe Recycle Pager Demo](https://github.com/filippella/SliderWidget/blob/master/demo.gif)

SliderWidget Demo

```java
public class MainActivity extends AppCompatActivity {

    private SliderView mSliderView;
    private TextView mSliderProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSliderView = (SliderView) findViewById(R.id.sliderView);
        mSliderProgress = (TextView) findViewById(R.id.sliderProgress);

        mSliderView.setListener(new SliderView.SliderListener() {
            @Override
            public void onSlide(float progress, SliderState state) {
                mSliderProgress.setText(String.format(Locale.UK, "Progress: %.0f", progress));
                switch (state) {
                    case OPENED:
                        startActivity(new Intent(MainActivity.this, SecondActivity.class));
                        break;
                }
            }
        });
    }
  }

```

License
-------

    Copyright 2016 Filippo Engidashet.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
