# HashAnalyzer
Analyzes the `hashcode()` function of a Java `class` with random instantiation and data analysis.

## Random Initialization
Using a still under-development/ beta software that primarily functions using [Java Reflection](https://docs.oracle.com/javase/tutorial/reflect/),
HashAnalyzer randomly instantiates `Object`s of a given class given the parameters of instantiation for each parameter in the constructor.
Currently, the HashAnalyzer can only analyze and instantiate `Object`s whose constructor exclusively contains primitive types (`int`,
`double`, `float`, `long`, `byte`, `short`, `char`, or `boolean`) or `String`s.

## Data Analysis
HashAnalyzer analyzes the `hashcode()` function of a given `class` by implementing various data analysis techniques
as well as offering the option to use the [Weka Data Mining Software](http://www.cs.waikato.ac.nz/ml/weka/ "Weka's Homepage"),
an open source collection of machine learning algorithms for data mining tasks created by [Waikato University)(http://www.waikato.ac.nz/).

Currently, the following `hashcode()` function analyses have been implemented or are in the late stages of development:
1. Frequency analysis
⋅⋅* Determines hot (frequent) and cold (infrequent) spots of the output hashcode values
⋅⋅* Calculates the standard deviation of the hashcodes
2. [Avalanche-effect](https://en.wikipedia.org/wiki/Avalanche_effect) analysis implemented by slightly altering the 
parameters used to initialize each `Object`. Then uses frequency analysis on the hashcode of the "similar" `Object`s 


### To Do:
[ ] Weigh the frequency of the randomly-generated parameters against the frequency analysis (possibly after classification) for
a more accurate and less biased frequency analysis

[ ] Create a recursive random instantiation class/method where the base case is if a parameter is primitive to allow inspection of `Object`s of any `class`.
