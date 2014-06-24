QuEst
=====

This open source software is aimed at quality estimation (QE) for machine translation. It was developed by Lucia Specia's team at the University of Sheffield and includes contributions from a number of researchers between Feb/2011 and Feb/2013. This particular release was made possible through the QuEst project (http://staffwww.dcs.shef.ac.uk/people/L.Specia/projects/quest.html). The code has two main parts: a feature extractor and a machine learning pipeline.

-----------------------------------------------------------------------

Feature extractor
================

This code implements a number of feature extractors, including most commonly used features in the literature, as well as many of the features used by systems submitted to the WMT2012 shared task on QE. Extractors for new features can be easily added (see the documentation under dist/).

Installation
============

The program itself does not require any installation step. It requires the Java Runtime Environment, and depending on the features to be extracted, a few additional libraries (see below). If you change the code, it can be easily rebuilt using NetBeans, as a NetBeans project is distributed in this release.

Dependencies
============

The libraries required to compile and run the code are included in the "lib" directory in the root directory of the distribution. The Java libraries should be included there when possible. Here is a list of libraries that should be downloaded and placed in the "lib" directory:

- Stanford POS Tagger 
- Berkeley Parser 

Apart from these lib files, QuEst requires other external tools / scripts to extract the specified features. The paths for these external tools are set in configuration file under config folder:

- TreeTagger (http://www.cis.uni-muenchen.de/~schmid/tools/TreeTagger/)
- SRILM (http://www.speech.sri.com/projects/srilm/download.html)
- Tokenizer (available from moses toolkit)
- Truecaser (available from moses toolkit)

Please note that above list is not exhaustive. Advance set of features require external tools, see details in corresponding section.



To compile
==========

ant -f build.xml

OR 

rebuild using NetBeans (NetBeans project files are distributed in this release)

To prepare:

(1) We provide the system some language resources. These are copied to lang_resources folder. Resources are available from here: http://www.quest.dcs.shef.ac.uk/

(2) You copy these to: lang_resources/[language]/

(3) Edit the configuration file (i.e. config/config_en-es.properties)

Running 
=======

We tested our software on Linux and Mac OS. We have not tested it on Windows yet. We provide shell scripts to call the feature extractor for a pre-defined list of features. 

For black box features:

./runBB.sh or bash runBB.sh

For glass box features:

./runGB_with_txt.sh or bash runGB.sh

Or

./runGB_with_xml.sh or bash runGB_with_xml.sh



More information about these scripts and the code itself can be found on the development guide (dist/MTFeatures.pdf).

Along with the code, we have provided configuration files and toy resources (SMT training corpus, language models, Giza files, etc) that should make the scripts above run without any problem. The actual resources used for the WMT12 shared task on QE You can download them from: http://www.quest.dcs.shef.ac.uk/

NOTE: One need to adapt the configuration file by providing the paths to the scripts where they are installed on your own system.
      i.e config/config_en-es.properties



Advance Features
================

For these features more information about the input resources and how they can be created for new language pairs can be found in specific readme files under the relevant resource folders (all under 'lang_resources') which could be downloaded from: http://www.quest.dcs.shef.ac.uk/


-----------------------------------------------------------------------

Machine learning pipeline
=========================

The function of this package of Python scripts is to build models for
machine translation (MT) quality estimation (QE). The input files are
a set of instances with features that describe sentence pairs (source
and target sentences). The features can be extracted using the FeatureExtractor
program as explained above.


Installation
============

The program itself does not require any installation step, it is just a matter
of running it provided that all the dependencies are installed.


Dependencies
============

All the machine learning algorithms are implemented by the scikit-learn library.
This program provides a command-line interface for some of the implementations
contained in this toolkit. In order to be able to run, the program requires
that the following packages are installed in your Python distribution:

- numpy ( http://scipy.org/Download )
- scikit-learn ( http://scikit-learn.org/stable/install.html )
- pyyaml ( http://pyyaml.org/ )


Running
=======

Note: Following commands are based on the assumption that all files are under 'learning' directory. 
The program takes only one input parameter, the configuration file. For
example:

python src/learn_model.py config/svr.cfg


Configuration file
==================

The configuration uses the YAML format (http://www.yaml.org/). Its layout is
quite straightforward. It is formed by key and value pairs that map directly
to dictionaries (in Python) or hash tables with string keys. One example is
as follows:

```
learning:
    method: LassoLars
    parameters:
        alpha: 1.0
        max_iter: 500
        normalize: True
        fit_intercept: True
        fit_path: True
        verbose: False
```

Each keyword followed by a ":" represents an entry in a hash. In this example,
the dictionary contains an entry "learning" that points to another dictionary
with two entries "method" and "parameters". The values of each entry can be
lists, dictionaries or primitive values like floats, integers, booleans or strings.
Please note that each level in the example above is indented with 4 spaces.

For more information about the YAML format please refer to http://www.yaml.org/ .

The configuration file is composed of three main parts: input and generic
options, feature selection, and learning.

Input comprises the following four parameters:

```
x_train: ./data/features/wmt2012_qe_baseline/training.qe.baseline.tsv
y_train: ./data/features/wmt2012_qe_baseline/training.effort
x_test: ./data/features/wmt2012_qe_baseline/test.qe.baseline.tsv
y_test: ./data/features/wmt2012_qe_baseline/test.effort
```

The first two are the paths to the files containing the features for the
training set and the responses for the training set, respectively. The
last two options refer to the test dataset features and response values,
respectively.

The format of the feature files is any format that uses a character to
separate the columns. The default is the tabulator char (tab, or '\t') as
this is the default format generated by the features extractor module.

Two other options are available:

```
scale: true
separator: "\t"
```

'scale' applies scikit-learn's scale() function to remove the mean and divide by
 the unit standard deviation for each feature. This function is applied to
 the concatenation of the training and test sets. More information about the
 scale function implemented by scikit-learn can be found at
 http://scikit-learn.org/dev/modules/generated/sklearn.preprocessing.scale.html

'separator' sets the character used to delimit the columns in the input files.

Configuration files for some of the implemented algorithms are available in the config/
directory.


Available algorithms
====================

Currently these are the algorithms available in the script:

* SVR: epsilon Support Vector Regression
The parameters exposed in the "Parameters" section of the configuration file are:
	- C
	- epsilon
	- kernel
	- degree
	- gamma
	- tol
	- verbose

Documentation about these parameters is available at
http://scikit-learn.org/stable/modules/generated/sklearn.svm.SVR.html#sklearn.svm.SVR

* SVC: C-Support Vector Classification
The parameters exposed in the "Parameters" section of the configuration file are:
	- C
	- coef0
	- kernel
	- degree
	- gamma
	- tol
	- verbose

Documentation about these parameters is available at
http://scikit-learn.org/stable/modules/generated/sklearn.svm.SVC.html#sklearn.svm.SVC

* LassoCV: Lasso linear model with iterative fitting along a regularization path.
The best model is selected by cross-validation.
The parameters exposed in the "Parameters" section of the configuration file are:
	- eps
	- n_alphas
	- normalize
	- precompute
	- max_iter
	- tol
	- cv
	- verbose

Documentation about these parameters is available at
http://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LassoCV.html#sklearn.linear_model.LassoCV


* LassoLars: Lasso model fit with Least Angle Regression (Lars)
The parameters exposed in the "Parameters" section of the configuration file are:
	- alpha
	- fit_intercept
	- verbose
	- normalize
	- max_iter
	- fit_path

Documentation about these parameters is available at
http://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LassoLars.html#sklearn.linear_model.LassoLars

* LassoLarsCV: Cross-validated Lasso using the LARS algorithm
The parameters exposed in the "Parameters" section of the configuration file are:
    - max_iter
	- normalize
	- max_n_alphas
	- n_jobs
	- cv
	- verbose

Documentation about these parameters is available at
http://scikit-learn.org/stable/modules/generated/sklearn.linear_model.LassoLarsCV.html#sklearn.linear_model.LassoLarsCV


Parameter optimization
======================

It is possible to optimize the parameters of the chosen method. This option is
set by the "optimize" setting under "learning" in the configuration file. The
script uses the scikit-learn's GridSearchCV implementation of grid search to
optimize parameters using cross-validation. To optimize the C, gamma and
epsilon parameters for the SVR, the learning section of the configuration
file could look as follows:

```
learning:
    method: SVR
optimize:
    kernel: [rbf]
    C: [1, 10, 2]
    gamma: [0.0001, 0.01, 2]
    epsilon: [0.1, 0.2, 2]
    cv: 3
    n_jobs: 1
    verbose: True
```

The parameter kernel is a list of strings representing the available kernels
implemented by scikit-learn. In this example only the "RBF" kernel is used.

* The SVR parameters C, gamma and epsilon are set with lists with 3 indexes:
    - the beginning of the range (begin value included)
    - the end of the range (end value included)
    - the number of samples to be generated within [beginning, end]

* The remaining parameters modify the behavior of the GridSearchCV class:
    - cv is the number of cross-validation folds
    - n_jobs is the number of parallel jobs scheduled to run the CV process
    - verbose is a boolean or integer value indicating the level of verbosity

For more information about the GridSearchCV class please refer to
http://scikit-learn.org/stable/modules/generated/sklearn.grid_search.GridSearchCV.html#sklearn.grid_search.GridSearchCV


Feature selection
=================

Another possible option is to perform feature selection prior to the learning
process. To set up a feature selection algorithm it is necessary to add the
"feature_selection" section to the configuration file. This section is
independent of the "learning" section:

```
feature_selection:
    method: RandomizedLasso
    parameters:
        cv: 10

learning:
    ...
```

Currently, the following feature selection algorithms are available:

* RandomizedLasso: works by resampling the training data and computing a Lasso
on each resampling. The features selected more often are good features.
The exposed parameters are:

    - alpha
    - scaling
    - sample_fraction
    - n_resampling
    - selection_threshold
    - fit_intercept
    - verbose
    - normalize
    - max_iter
    - n_jobs

These parameters and the method are documented at:
http://scikit-learn.org/stable/modules/generated/sklearn.linear_model.RandomizedLasso.html#sklearn.linear_model.RandomizedLasso


* ExtraTreesClassifier: meta estimator that fits a number of randomized decision trees (a.k.a. extra-trees) on various sub-samples of the dataset and use averaging to improve the predictive accuracy and control over-fitting.
The exposed parameters are:

    - n_estimators
    - max_depth
    - min_samples_split
    - min_samples_leaf
    - min_density
    - max_features
    - bootstrap
    - compute_importances
    - n_jobs
    - random_state
    - verbose

Documentation about the parameters and the method can be found at:
http://scikit-learn.org/stable/modules/generated/sklearn.ensemble.ExtraTreesClassifier.html#sklearn.ensemble.ExtraTreesClassifie


Learning with Gaussian Process
==============================

The function of this package of Python scripts is to build models for
machine translation (MT) quality estimation (QE) using Gaussian Process. The input files are
a set of instances with features that describe sentence pairs (source
and target sentences). 

Installation
============

The program itself does not require any installation step, it is just a matter
of running it provided that all the dependencies are installed.


Dependencies
============

All the machine learning algorithms are implemented by the GPy library.
This program provides a command-line interface for some of the implementations
contained in this toolkit. In order to be able to run, the program requires
that the following packages are installed in your Python distribution:

- GPy ( https://pypi.python.org/pypi/GPy )
- sciPy ( http://scipy.org/Download )

Running
=======

Note: Following commands are based on the assumption that all files are under 'learning' directory. 
The program takes only one input parameter, the configuration file. For
example:

python src/GP_wmt_regression.py

Please set the path in above script to the input files.
e.g
X = np.genfromtxt('train-79-features.qe.tsv')
test_X = np.genfromtxt('test-79-features.qe.tsv')
Y = np.genfromtxt('qe_reference_en-es.train.effort').reshape(-1, 1)
test_Y = np.genfromtxt('qe_reference_en-es.test.effort').reshape(-1, 1)



Acknowledgements
================

A number of people contributed to this code:

-- Catalina Hallett, who provided an earlier version of the feature extraction code.
-- José Guilherme Camargo de Souza, who took care of the machine learning pipeline and helped with the feature extraction code in various ways. 
-- Kashif Shah, who is the main person taking care of the code nowadays.
-- All of the following who visited Sheffield and added their own feature extractors to the code and also contributed with testing it:
   -- Eleftherios Avramidis
   -- Christian Buck 
   -- David Langlois
   -- Erwan Moreau
   -- Quang Ngoc Luong 
   -- Raphael Rubino 

We thank the European commission Pascal2 Network of Excellence for the funding the visits of these and other researchers to Sheffield under the "Harvest" scheme, as well as the European commission FP7 QTLauncePad CSA for funding further development and maintenance of this software. 


License
=======
The license for the Java code and any python and shell scripts developed here is the very permissive BSD License (http://en.wikipedia.org/wiki/BSD_licenses). For pre-existing code and resources, e.g., scikit-learn and Berkeley parser, please check their websites. 
