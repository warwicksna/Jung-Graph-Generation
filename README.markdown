# Graph2SQL

The `graph2sql.pl` script takes as input any number of graphml formatted files and outputs a sequence of sql statements to place the data in the required data format for SNAT.

## Usage

To run the code:

    perl graph2sql.pl *.graphml

This will read each a.graphml file in the current directory and output a.sql in the same directory.