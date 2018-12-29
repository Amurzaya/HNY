import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class NY_greetings extends PApplet {

//PFont greeting;
PImage apipLogo;
float theta =PI/8;   
float a;
float centerX;
float centerY;
float currentX;
float currentY;
float newX;
float newY;
float newZ;
Star[] stars = new Star[800];
float speed;

float z=1;
public void setup() {
  
  background(0);
  
  for (int i = 0; i < stars.length; i++) {
    stars[i] = new Star();
  }
  apipLogo= loadImage("APIP new logo_without circle-04.png");
   /* greeting = loadFont("OpenSans-Bold-72.vlw");
    fill(255);
 textFont(greeting);
 textSize(4);
textAlign(CENTER);
text("HAPPY NEW YEAR!",0,0,0);
  currentX = mouseX;
  currentY = mouseY; */
}

public void draw() {
  noFill();
translate(width/2, height/2,0);
point(20,20);
background(5,20,115);
strokeWeight(0.5f);
//image(apipLogo,-50,30,100,40);
//ellipse(0,0, height/2,height/2);
 speed = map(mouseY, 0, height, 0, 10);
  for (int i = 0; i < stars.length; i++) {
    stars[i].update();
    stars[i].show();
  }
  camera(mouseX, mouseY, 120.0f, 50.0f, 50.0f, mouseX/PI, 
       0.0f, 1.0f, 0.0f);
       rotateX(-PI/6);
rotateY(PI/3);
frameRate(10);

//blendMode(OVERLAY);
 
rotateX(120);
strokeWeight(0.6f);
stroke(255,MULTIPLY/2);
  // Let's pick an angle 0 to 90 degrees based on the mouse position
   float a = (millis()/ (float) 1000) * 90f;
  // Convert it to radians
  theta = radians(a);
  // Start the tree from the bottom of the screen
 
  // Draw a line 120 pixels
  //line(0,0,0,-120);
  // Move to the end of that line
  // Start the recursive branching!
scale(2);
 branch(110,50);
 //add blur (smooth edges = smooth elevation when moving to 3D)
 //saveFrame("greeting-######.png");
}

public void branch(float h, float j) {
    //noStroke();
    stroke(255,MULTIPLY/2);
    strokeWeight(1);
    //fill(255);
  h *= 0.54f;
   j *= 0.24f;
  // All recursive functions must have an exit condition!!!!
  // Here, ours is when the length of the branch is 2 pixels or less
  if (h > 1.2f) {
    pushMatrix();    // Save the current state of transformation (i.e. where are we now)
    rotateZ(theta);   // Rotate by theta
    fill(255,SCREEN/4);
beginShape(TRIANGLE_STRIP);

vertex(-j, -j, -j);
vertex( j, -j, -j);
vertex(  0,   0, j);
    fill(0,160,240,MULTIPLY/2);
vertex( j, -j, -j);
vertex( j,  j, -j);
vertex(   0,   0,  j);
fill(180,30,240,MULTIPLY/2);
vertex( j, j, -j);
vertex(-j, j, -j);
vertex(   0,   0,  j);
fill(30,10,180,MULTIPLY/2);
vertex(-j,  j, -j);
vertex(-j, -j, -j);
vertex(   0,    0,  j);
endShape(CLOSE);

    //line(0, 30, 0, -h);  // Draw the branch
    translate(theta, +h, +j); // Move to the end of the branch
    branch(j, h);       // Ok, now call myself to draw two new branches!!
    popMatrix();     // Whenever we get back here, we "pop" in order to restore the previous matrix state
    
    // Repeat the same thing, only branch off to the "left" this time!
    pushMatrix();
    rotateZ(-theta);
    rotateY(theta);
    rotateX(z);
    //line(0, 0, 0, -h,0,0);
    translate(0,h,j);

     //box(j,j,j);
    branch(h,h);
   
    popMatrix();
  
    
  }
   if(h<4 && h>10) {
filter(BLUR,4);
 }



  }
  
  public void mouseDragged() {
  newX = mouseX;
  newY = -mouseY;
}


class Star {
  // I create variables to specify the x and y of each star.
  float x;
  float y;
  // I create "z", a variable I'll use in a formula to modify the stars position.
  float z;

  // I create an other variable to store the previous value of the z variable.
  // (the value of the z variable at the previous frame).
  float pz;

  Star() {
    // I place values in the variables
    x = random(-width/2, width/2);
    // note: height and width are the same: the canvas is a square.
    y = random(-height/2, height/2);
    // note: the z value can't exceed the width/2 (and height/2) value,
    // beacuse I'll use "z" as divisor of the "x" and "y",
    // whose values are also between "0" and "width/2".
    z = random(width/2);
    // I set the previous position of "z" in the same position of "z",
    // which it's like to say that the stars are not moving during the first frame.
    pz = z;
  }

  public void update() {
    // In the formula to set the new stars coordinates
    // I'll divide a value for the "z" value and the outcome will be
    // the new x-coordinate and y-coordinate of the star.
    // Which means if I decrease the value of "z" (which is a divisor),
    // the outcome will be bigger.
    // Wich means the more the speed value is bigger, the more the "z" decrease,
    // and the more the x and y coordinates increase.
    // Note: the "z" value is the first value I updated for the new frame.
    z = z - speed;
    // when the "z" value equals to 1, I'm sure the star have passed the
    // borders of the canvas( probably it's already far away from the borders),
    // so i can place it on more time in the canvas, with new x, y and z values.
    // Note: in this way I also avoid a potential division by 0.
    if (z < 1) {
      fill(255,30);
      z = width/2;
      x = random(-width/2, width/2);
      y = random(-height/2, height/2);
      pz = z;
    }
  }

  public void show() {
    fill(255);
    noStroke();

    // with theese "map", I get the new star positions
    // the division x / z get a number between 0 and a very high number,
    // we map this number (proportionally to a range of 0 - 1), inside a range of 0 - width/2.
    // In this way we are sure the new coordinates "sx" and "sy" move faster at each frame
    // and which they finish their travel outside of the canvas (they finish when "z" is less than a).

    float sx = map(x / z, 0, 1, 0, width/4);
    float sy = map(y / z, 0, 1, 0, height/4);;

    // I use the z value to increase the star size between a range from 0 to 16.
    float r = map(z, 0, width/2, 1, 0);
    ellipse(sx, sy, r*2, r*2);

    // Here i use the "pz" valute to get the previous position of the stars,
    // so I can draw a line from the previous position to the new (current) one.
    float px = map(x / pz, 0, 1, 0, width/4);
    float py = map(y / pz, 0, 1, 0, height/4);

    // Placing here this line of code, I'm sure the "pz" value are updated after the
    // coordinates are already calculated; in this way the "pz" value is always equals
    // to the "z" value of the previous frame.
    pz = z;

    stroke(255);
    line(px, py, sx, sy);

  }
}
  public void settings() {  size(800, 600, P3D);  smooth(BLUR); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "NY_greetings" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
