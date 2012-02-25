#!/usr/bin/env perl

use v5.10.0;
use warnings;
use strict;

use XML::Parser;

# Graph type is undirected for undirected graphs, directed for directed graphs
my $graphtype = "undirected";

# Subroutines called when the parser finds a matching element
my $events = {
    graph => \&element_graph,
    node  => \&element_node,
    edge  => \&element_edge,
};

# Parse the xml file
my $parser = XML::Parser->new(Handlers => {
	Start => \&element_start,
});

# File to output sql commands to
my $fh;

foreach (@ARGV) {
    my $infile = $_;
    my $outfile = $infile;
    $outfile =~ s/\..*$/\.sql/;
    
    open $fh, ">", $outfile or die $!;
    database_structure_sql();
    
    $parser->parsefile($infile);
}

# Called when an element start tag is found
# Will call element_graph, element_node, element_edge if graph, node or edge element is found
sub element_start {
    my ($expat, $element, %attributes) = @_;
    $element = lc $element;
    
    if (exists $events->{$element})
    {
        $events->{$element}(%attributes);
    }
}

# The graph element can indicate whether a node is directed or undirected
sub element_graph {
    my %attributes = @_;
    return if !exists $attributes{edgedefault};
    
    $graphtype = lc $attributes{edgedefault};    
}

# Add a vertex to the graph
sub element_node {
    my %attributes = @_;
    return if !exists $attributes{id};
        
    print $fh "INSERT INTO \`Entity\` (\`EntityName\`) VALUES ($attributes{id});\n";
}

# Add an edge to the graph
sub element_edge {
    my %attributes = @_;
    return if !exists $attributes{source};
    return if !exists $attributes{target};
    
    print $fh insert_to_connected_sql($attributes{source}, $attributes{target});
    print $fh insert_to_connected_sql($attributes{target}, $attributes{source}) if $graphtype =~ m/^undirected/;
}

# The insert edge sql code is a little complicated, so it gets its own function
sub insert_to_connected_sql {
    my ($source, $target) = @_;
    
    my $sql;
    
    $sql = 'INSERT INTO `Connected` (`FromEntityID`, `ToEntityID`, `Strength`, `Initial`) (' ."\n\t";
    $sql .= 'SELECT `Entity1`.`EntityID` AS `FromEntityID`, `Entity2`.`EntityID` AS `ToEntityID`, 1 AS `Strength`, 1 AS `Initial`' . "\n\t";
    $sql .= 'FROM `Entity` AS `Entity1`, `Entity` AS `Entity2`' . "\n\t";
    $sql .= "WHERE \`Entity1\`.\`EntityName\` = '$source' AND \`Entity2\`.\`EntityName\` = '$target'\n";
    $sql .= ');' . "\n";
}

# outputs the database structure as sql
sub database_structure_sql {
    print $fh <<SQL;
CREATE TABLE IF NOT EXISTS `Connected` (
  `FromEntityID` int(11) NOT NULL,
  `ToEntityID` int(11) NOT NULL,
  `Strength` float NOT NULL DEFAULT '1',
  `Initial` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`FromEntityID`,`ToEntityID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `Entity` (
  `EntityID` int(11) NOT NULL AUTO_INCREMENT,
  `EntityName` varchar(150) NOT NULL,
  PRIMARY KEY (`EntityID`),
  UNIQUE KEY `EntityName` (`EntityName`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `MessageProperties` (
  `MessageID` int(11) NOT NULL AUTO_INCREMENT,
  `Data` text NOT NULL,
  `TimeStamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Restricted` tinyint(1) NOT NULL DEFAULT '1',
  `IDonNetwork` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`MessageID`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `MessageReceive` (
  `MessageID` int(11) NOT NULL,
  `EntityID` int(11) NOT NULL,
  PRIMARY KEY (`MessageID`,`EntityID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `MessageSend` (
  `MessageID` int(11) NOT NULL,
  `EntityID` int(11) NOT NULL,
  PRIMARY KEY (`MessageID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

SQL
}
