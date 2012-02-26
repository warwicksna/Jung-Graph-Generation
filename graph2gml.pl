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
	End => \&element_end,
});

# File to output sql commands to
my $fh;

my $indent = 0;

foreach (@ARGV) {
    my $infile = $_;
    my $outfile = $infile;
    $outfile =~ s/\..*$/\.gml/;
    
    open $fh, ">", $outfile or die $!;
    
    $parser->parsefile($infile);
}

# Called when an element start tag is found
# Will call element_graph, element_node, element_edge if graph, node or edge element is found
sub element_start {
    my ($expat, $element, %attributes) = @_;
    $element = lc $element;
    
    return unless exists $events->{$element};
    
    $events->{$element}(%attributes);
    $indent++;
}

sub element_end {
    my ($expat, $element) = @_;
    $element = lc $element;
    
    return unless exists $events->{$element};

    $indent--;
    
    for (0..$indent - 1) {
        print $fh "\t";
    }
    
    print $fh "]\n";
}

# The graph element can indicate whether a node is directed or undirected
sub element_graph {
    print $fh "graph [\n";
    
    my %attributes = @_;
    return unless exists $attributes{edgedefault};
    
    $graphtype = lc $attributes{edgedefault};
    my $type = ($graphtype =~ m/^undirected$/) ? 0 : 1;
    print $fh "\tdirected $type\n"; 
}

# Add a vertex to the graph
sub element_node {
    print $fh "\tnode [\n";
    
    my %attributes = @_;
    return unless exists $attributes{id};
    
    print $fh "\t\tid $attributes{id}\n";
    print $fh "\t\tlabel \"$attributes{id}\"\n";
}

# Add an edge to the graph
sub element_edge {
    print $fh "\t edge [\n";
    
    my %attributes = @_;
    return unless exists $attributes{source};
    return unless exists $attributes{target};
    
    print $fh "\t\tsource $attributes{source}\n";
    print $fh "\t\ttarget $attributes{target}\n";
    print $fh "\t\tlabel \"Edge from node $attributes{source} to node $attributes{target}\"\n";
}