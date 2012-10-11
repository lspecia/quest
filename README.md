QuEst
=====

Machine translation (MT) quality estimation (QE) open source system that implements feature extractors for all or most of the information sources
used in systems submitted to the shared task, and possibly new features, and that can be customized
to particular applications.

How to implement a feature?
===========================

Features extend an abstract class called "Feature". From the class
documentation:

"
 Feature is an abstract class which models a sentence feature. Typically, a
  Feature consist of a value, a procedure for calculating the value and a set
  of dependencies, i.e., resources that need to be available in order to be
  able to compute the feature value. <br> Classes extending Feature will have
  to provide their own method for computing the feature value by
implementing run()
 "

Each feature should provide a set with the name of the resources
(external linguistics processors that run in the preprocessing step)
required to process the feature. The resources are managed by a class
called ResourceManager that keeps track of the resources currently
handled by the system.

If a system requires a resource that has not been registered/processed
previously, it is necessary to create a wrapper that implements and
registers it.


How to implement a resource?
============================

Different preprocessors extend the abstract class "Resource". One
example is the Tokenizer class that implements a wrapper for the Moses
tokenizer:

src/wlv/mt/tools/Tokenizer.java

The extended classes usually run the external code by spawning a new
process (using Java's ProcessBuilder class). The results can then be
retrieved in the form of output files by the Java code. More examples
of different cases are in src/wlv/mt/tools/


Dependencies
============

- Stanford POS Tagger ( http://nlp.stanford.edu/software/tagger.shtml#Download )