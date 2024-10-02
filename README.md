This repository is retired.
New developments happen in the
[mathosphere.](https://github.com/TU-Berlin/mathosphere/tree/master/mlp)

Mathematical Language Processing
================================
[![Build Status](https://travis-ci.org/TU-Berlin/project-mlp.svg?branch=master)](https://travis-ci.org/TU-Berlin/project-mlp)

# Run
* compile the maven project
* adapt the paths to your stratosphere environment in the file `cluster-run.sh`
* setup the right values for the parameters of the ranking algorithm also in `cluster-run.sh`
* execute the script


## Notice
To start the processor, an additional model file is needed. Download the Stanford POS tagger from http://nlp.stanford.edu/software/tagger.shtml. Within this archive is a directory called `pos-tagger-models/`, containing a variaty of model files for a couple of languages.

If uncertain, the `english-left3words-distsim.tagger` model is a good starting point.

Tested with http://nlp.stanford.edu/software/stanford-postagger-2012-11-11.zip ... the most recent version 
http://nlp.stanford.edu/software/stanford-postagger-2014-01-04.zip
is currently beeeing tested.

## Log

To trace was was done on the MLP server
install stratosphere via debian package
physikerwelt@mlp:~/stanford-postagger-2014-01-04/models$ cp english-left3words-distsim.tagger ~
