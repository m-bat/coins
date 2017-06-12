#!/usr/bin/perl

$pattern = "";
$code = 0;
while (<>) {
    chop;
    if (/^\#/) {
	# skip comments and cpp lines
    } elsif (/^==(\S+)/) {
	$code = $1;
    } elsif (/^=!(.*)/) {
	$pattern = $1;
    } elsif (/^=$/) {
	$pattern = "";
    } elsif ($pattern && /^(-?)(\S+)\s*$/) {
	$orig = $2;
	$typed = $1 eq '-' ? "false" : "true";
	$line = $pattern . "\n";
	$line =~ s/%x/$orig/g;
	$line =~ s/%n/$code/g;
	$line =~ s/%t/$typed/g;
	$line =~ s/%%/%/g;
	print $line;
	$code++;
    } else {
	print "$_\n";
    }
}
