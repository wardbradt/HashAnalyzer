Hashes
===================
##hashCode runtime
| Class         | hashCode runtime (nanoseconds)  |
| ------------- |:-------------:                  |
| String        | 233                     		  |
| EnglishWord   | 729            				  |
| Integer       | 329         				      | 

Despite how simple I thought my EnglishWord hashCode method was, its runtime at least almost doubled that of Integer and String. I would also assume their hashCode methods are much better than mine.

##Hot and Cold Spots
###EnglishWord
The least common hash codes are in the areas of: 
1. 7 occurs 10 times.
2. 33 occurs 10 times.
3. 50 occurs 10 times.
And the most common hash codes are in the areas of: 
1. 0 occurs 29 times.
2. 36 occurs 27 times.
3. 48 occurs 25 times.
###String
The least common hash codes are in the areas of: 
1. 95 occurs 8 times.
2. 157 occurs 8 times.
3. 61 occurs 9 times.
And the most common hash codes are in the areas of: 
1. 33 occurs 33 times.
2. 51 occurs 26 times.
3. 141 occurs 25 times.

Now I'm not going to say I am better than Oracle, but I did create a class very similar to `java.lang.String` wherein my least frequent `hashCode`s (7, 33, and 50) were more common (than 95, 157, and!! 61) and my most common hashCode (0) is less frequent (than 33), thus rendering it a more continuous `hashCode` method.


##Continuity Analysis
| Class    | Average Differentiation | Continuous?  |
| ------------- |:-------------:| -----:|
| EnglishWord   | 4.93 			| `true` |
| String        | 4.82 			| `true` |
| Integer       | 8.53 			| `true` |

The "average differentiation" of `EnglishWord` and `String` are roughly equal, so the minor win I thought I had in "Hot and Cold Spots" does not matter. This means they are similarly distributed. They are all continuous, which definitely makes sense for EnglishWord's hashCode method as well as Integer's.
##Avalanche-Effect Analysis
`EnglishWord` definitely fails the avalanche-effect acid test. Barely changing its constructor's only parameter (the change being changing one of its characters to the next ascii value) only changes the hashCode function by 1. String follows the same unfortunate convention, as does Integer.

##Raw Data
####EnglishWord hashCode Raw Heat Map: 
0 xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
1 xxxxxxxxxxxxxxxxxx
2 xxxxxxxxxxx
3 xxxxxxxxxxxxxxxxx
4 xxxxxxxxxxxxxxxxxxxxx
5 xxxxxxxxxxxxxxxx
6 xxxxxxxxxxxxxxxxx
7 xxxxxxxxxx
8 xxxxxxxxxxxxxxxxxx
9 xxxxxxxxxxxxxxxx
10 xxxxxxxxxxx
11 xxxxxxxxxxx
12 xxxxxxxxxxxxxxxxxxxxx
13 xxxxxxxxxxxxxxxxx
14 xxxxxxxxxxxxxxx
15 xxxxxxxxxxxxxxx
16 xxxxxxxxxxxxxxxxxxxxxx
17 xxxxxxxxxxxxxxxxxxxxx
18 xxxxxxxxxxxx
19 xxxxxxxxxxxxxx
20 xxxxxxxxxxxxxxxxxxxxxxxx
21 xxxxxxxxxxxxxxxxxxxxxxxxx
22 xxxxxxxxxxxxxxxxxxxx
23 xxxxxxxxxxxxxxxx
24 xxxxxxxxxxxxxxxxxx
25 xxxxxxxxxxxxx
26 xxxxxxxxxxxxxxxxxx
27 xxxxxxxxxxxxxxxxxxxxxxxx
28 xxxxxxxxxxxxxxxxx
29 xxxxxxxxxxxxxxxxxxxxxxx
30 xxxxxxxxxxxxxxxxxxx
31 xxxxxxxxxxxxxxxx
32 xxxxxxxxxxxxxxx
33 xxxxxxxxxx
34 xxxxxxxxxxxxxxxxxxxxxxx
35 xxxxxxxxxxxxxxxxxxxxxxx
36 xxxxxxxxxxxxxxxxxxxxxxxxxxx
37 xxxxxxxxxxxxxxxxx
38 xxxxxxxxxxxx
39 xxxxxxxxxxxxx
40 xxxxxxxxxxx
41 xxxxxxxxxxxxxxxxxxx
42 xxxxxxxxxxxx
43 xxxxxxxxxxxxxxxx
44 xxxxxxxxxxxxxxxxxxxxxxx
45 xxxxxxxxxxxxxxxxxxxxx
46 xxxxxxxxxxxxxxxxxx
47 xxxxxxxxxxxxx
48 xxxxxxxxxxxxxxxxxxxxxxxxx
49 xxxxxxxxxxxxxxx
50 xxxxxxxxxx
51 xxxxxxxxxxx
52 xxxxxxxxxxxxxxxxx
53 xxxxxxxxxxxxxxxx
54 xxxxxxxxxxxxxxxxxxxxxxx
55 xxxxxxxxxxxxx
56 xxxxxxxxxxxxxxxxxxx
57 xxxxxxxxxxxxxxxxxx
58 xxxxxxxxxxxxxxxxxxx

####String hashCode Raw Heat Map: 
33 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
35 xxxxxxxxxx
37 xxxxxxxxxxxxx
39 xxxxxxxxxxxxxxxxxx
41 xxxxxxxxxxxxxx
43 xxxxxxxxxxxxxxxxxxxxx
45 xxxxxxxxxxxxxxxxxxxx
47 xxxxxxxxxxxxxxxxx
49 xxxxxxxxxxxxxxxxx
51 xxxxxxxxxxxxxxxxxxxxxxxxxx
53 xxxxxxxxxxxxxxxxxx
55 xxxxxxxxxxxxxxxx
57 xxxxxxxxxxxxxxxxxxxxx
59 xxxxxxxxxxxxxxxxxxxx
61 xxxxxxxxx
63 xxxxxxxxxxxx
65 xxxxxxxxxxx
67 xxxxxxxxxxxxxxxxxxxxxxx
69 xxxxxxxxxxxx
71 xxxxxxxxxxxxxxxxx
73 xxxxxxxxxxxxxxxx
75 xxxxxxxxxxxx
77 xxxxxxxxxxxxx
79 xxxxxxxxxxxxxxxxxxxx
81 xxxxxxxxxxxxxxxxxxx
83 xxxxxxxxxxxxxxxxxx
85 xxxxxxxxxxxxxxxxx
87 xxxxxxxxxxxx
89 xxxxxxxxxxxxxxxxx
91 xxxxxxxxxxxxxxxxxxx
93 xxxxxxxxxxxxxxxxxxx
95 xxxxxxxx
97 xxxxxxxxxxxxxxxxx
99 xxxxxxxxxxx
101 xxxxxxxxxxxxx
103 xxxxxxxxxxxxxxxxxx
105 xxxxxxxxxxx
107 xxxxxxxxxxxxxxxxxx
109 xxxxxxxxxxxxxxxxx
111 xxxxxxxxxxxxxxxxxxx
113 xxxxxxxxxxxxxxxxx
115 xxxxxxxxxxxxxxxxx
117 xxxxxxxxxxxxxxx
119 xxxxxxxxxxxxxxxx
121 xxxxxxxxxx
123 xxxxxxxxxxxxxxxxxxxx
125 xxxxxxxxxxxxxxx
127 xxxxxxxxxxxxxxxxx
129 xxxxxxxxxxxxxxxxxx
131 xxxxxxxxxxxxxxxxx
133 xxxxxxxxxxxxx
135 xxxxxxxxxxxxxxxxxx
137 xxxxxxxxxxxxxxx
139 xxxxxxxxxxxxx
141 xxxxxxxxxxxxxxxxxxxxxxxxx
143 xxxxxxxxxxxxxxxxxx
145 xxxxxxxxxxxxxxxxxxxxx
147 xxxxxxxxxxxxxxx
149 xxxxxxxxx
151 xxxxxxxxxxxx
153 xxxxxxxxxxxxxxxxxxxxxxx
155 xxxxxxxxxx
157 xxxxxxxx

####Integer hashCode Raw Heat Map: 
0 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
1 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
2 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
3 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
4 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
5 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
6 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
7 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
8 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
9 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
10 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
11 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
12 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
13 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
14 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
15 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
16 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
17 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
18 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
19 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
> Written with [StackEdit](https://stackedit.io/).
> Ward Bradt, Jimmy Pham, Moe Sunami