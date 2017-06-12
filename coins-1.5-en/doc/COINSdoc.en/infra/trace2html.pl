#!/usr/bin/perl
#
# Make HTML file from backend trace output.

$prefix = "trace";
$withc = 0;

while ($ARGV[0] =~ /^-/) {
    if ($ARGV[0] =~ /^-o/) {
	shift;
	$prefix = shift;
    }
    elsif ($ARGV[0] =~ /^-c/) {
	shift;
	$withc = 1;
    }
    else {
	die "Usage: trace2html.pl [-c -o prefix] tracefile...\nBad option: $ARGV[0]\n";
    }
}

$mainfile = "$prefix.html";
$indexfile = "$prefix-index.html";
$bodyfile = "$prefix-body.html";
$srcfile = "$prefix-src.html";

@header = (
    "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\">\n",
    "<html lang=\"ja\">\n",
    "<head>\n",
    "  <meta HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=ISO-2022-JP\">\n",
    "  <title>COINS Backend Trace</title>\n",
    "</head>\n"
 );

@tailer = ("</html>\n");


@framewithoutc = (
    "<frameset cols=\"20%,80%\">\n",
    "<frame src=\"$indexfile\" name=\"indexFrame\">\n",
    "<frame src=\"$bodyfile\" name=\"bodyFrame\">\n",
    "</frameset>\n",
    "<noframes>\n",
    "<h2>\n",
    "Frame Alert</h2>\n",
    "\n",
    "<p>\n",
    "This document is designed to be viewed using the frames feature. If you see this message, you are using a non-frame-capable web client.\n",
    "<br>\n",
    "Link to<a href=\"trace-index.html\">Non-frame version.</a></noframes>\n"
);


@framewithc = (
    "<frameset cols=\"20%,80%\">\n",
    "<frame src=\"$indexfile\" name=\"indexFrame\">\n",
    "<frameset rows=\"30%,70%\">\n",
    "<frame src=\"$srcfile\" name=\"srcFrame\">\n",
    "<frame src=\"$bodyfile\" name=\"bodyFrame\">\n",
    "</frameset>\n",
    "</frameset>\n",
    "<noframes>\n",
    "<h2>\n",
    "Frame Alert</h2>\n",
    "\n",
    "<p>\n",
    "This document is designed to be viewed using the frames feature. If you see this message, you are using a non-frame-capable web client.\n",
    "<br>\n",
    "Link to<a href=\"trace-index.html\">Non-frame version.</a></noframes>\n"
);




open BODY,">$bodyfile" or die;
open INDEX,">$indexfile" or die;

print INDEX @header;
print INDEX "<body>\n<pre>\n";

print BODY @header;
print BODY "<body>\n<pre>\n";

$phasenum = 0;

%linehash = ();

while (<>) {
    if (/^(Before|After)\s*(.*)/) {
	$tag = $1;
	$phase = $2;
	chomp;
	$line = &quoteline($_);
	$phasenum++;
	print BODY "<a name=\"link$phasenum\">$line</a>\n";
	print INDEX "<a href=\"$bodyfile#link$phasenum\" target=\"bodyFrame\">$line</a>\n";
    }
    elsif (/^Module\s*"([^\"]*)"/) {
	$srcname = $1;
	print BODY &quoteline($_);
	&parsemodule();
    }
    elsif (/^Function\s*"([^\"]*)"/) {
	$funcname = $1;
	print BODY &quoteline($_);
	&parsefunction();
    }
    else {
	print BODY &quoteline($_);
    }
}

print BODY "</pre>\n</body>\n";
print BODY @tailer;

close BODY;

print INDEX "</pre>\n</body>\n";
print INDEX @tailer;

close INDEX;

if ($withc && $srcname ne '') {
    open CSRC,"<$srcname" or die;
    open CHTML,">$srcfile" or die;
    print CHTML @header;
    print CHTML "<body>\n<pre>\n";
    $lineno = 1;
    while (<CSRC>) {
	chomp;
	$line = &quoteline($_);
	print CHTML "<a name=\"CS$lineno\">$line</a>";
	if ($chapter{$lineno} != 0) {
	    print CHTML "&nbsp;<a href=\"$bodyfile#L$lineno-0\" target=\"bodyFrame\">[&gt;&gt]</a>";
	}
	print CHTML "\n";
	$lineno++;
    }
    print CHTML "</pre>\n</body>\n";
    print CHTML @tailer;
    close CHTML;
    close CSRC;
}

open MAIN,">$mainfile" or die;
print MAIN @header;
if ($withc && $srcname ne '') {
    print MAIN @framewithc;
} else {
    print MAIN @framewithoutc;
}
print MAIN @tailer;
close MAIN;


exit 0;


sub parsemodule {
    while (<>) {
	print BODY &quoteline($_);
	if (/^Function\s*"([^\"]*)"/) {
	    &parsefunction($1);
	}
	elsif (/^Global Symbol/) {
	    &parsesymtab();
	}
	elsif (/^End Module/) {
	    last;
	}
    }
}

sub printlink {
    my ($space, $body, $key, $withc) = @_;

    if ($linehash{$key} == $phasenum) {
	print BODY "$_\n";
    } else {
	$linehash{$key} = $phasenum;
	$chapter = $chapter{$key} + 0;
	print BODY "$space<a name=\"L$key-$chapter\">$body</a>";
	$back = $chapter - 1;
	$forw = $chapter + 1;
	if ($back >= 0) {
	    print BODY "<a href=\"#L$key-$back\">[&lt;&lt;]</a>";
	}
	print BODY "<a href=\"#L$key-$forw\">[&gt;&gt;]</a>";
	if ($withc) {
	    print BODY "<a href=\"$srcfile#CS$key\" target=\"srcFrame\">[source]</a>";
	}
	print BODY "\n";
	$chapter{$key}++;
    }
}

sub parsefunction {

    while (<>) {
	chomp;
	if (/^(\s*)\(([Ll][Ii][Nn][Ee])\s+(\d+)\)/) {
	    &printlink($1, "($2 $3)", $3 + 0, $withc);
	} elsif (/^(\s*)(\#\d+ Basic Block \(([^\)]+)\).*)/) {
	    &printlink($1, $2, $3, 0);
	} elsif (/^(\s*)(\(DEFLABEL\s+\"([^\"]+)\"\))/) {
	    &printlink($1, $2, $3, 0);
	} elsif (/^(\s*)(\(deflabel\s+([^\)]+)\))/) {
	    &printlink($1, $2, $3, 0);
	} else {
	    print BODY &quoteline($_),"\n";
	}
	last if /^End Function/;
    }
}

sub parsesymtab {
    while (<>) {
	print BODY &quoteline($_);
	last unless (/^\s+\(/);
    }
}

sub quoteline {
    my ($line) = @_;
    $line =~ s/</&lt;/g;
    $line =~ s/>/&gt;/g;
    return $line;
}
