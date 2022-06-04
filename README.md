this program takes a mathematical expression and calculates result step by step.

it supports:
- integer and floating point numbers
- \+ \- / * ^ ( ) 
- sin(), cos(), tan(), cot(), ln(), e(), log(), abs(), other function can be added easily
<br>

input 1:
```
e(sin(1+3) * log(2^2))
```
output 1:
```
0.6340417317497135
```
<br>

input 2:
```
-(-(2^3))/4+1 
```
output 2:
```
  3
```
<br>

input 3:
``` 
-(((1+2)*(-3))^(1+1)) 
```
ouput 3:
```
-81
```
<br>

input 4:
```
((1.4+1.6)*10)/100 
``` 
output 4:
```
0.3
```
<br>

some errors will be detected by program such as wrog use of parentheses and more. hree is some inputs that cause erorr:
```
)(1+3)*2
(((2+5)^2)+4
3+5^
+5^
5/0
```
<br>

for each expression, a cli version of expression-calculate-tree will be printed: 

![tree](https://user-images.githubusercontent.com/70153144/142826292-37bd0066-1964-454f-a66b-fc8a03124bc3.png)




