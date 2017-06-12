/*
 *  This software may only be used by you under license from AT&T Corp.
 *  ("AT&T").  A copy of AT&T's Source Code Agreement is available at
 *  AT&T's Internet website having the URL:
 *  <http://www.research.att.com/sw/tools/graphviz/license/source.html>
 *  If you received this software without first entering into a license
 *  with AT&T, you have an infringing copy of this software and cannot use
 *  it without violating AT&T's intellectual property rights.
 */

package att.grappa;

import java.awt.*;
import java.util.*;

public class GraphicContext implements Cloneable
{
  // given name, get color
  private static Hashtable colorTable  = new Hashtable(660,10);
  // given color, get name
  private static Hashtable colorLookUp = new Hashtable(660,10);
  // given canonical fontname, get standard fontname
  private static Hashtable fontLookUp = new Hashtable(12,6);
  
  // initialize colorTable
  static {
    doAddColor("aliceblue",new Color(240,248,255));
    doAddColor("antiquewhite",new Color(250,235,215));
    doAddColor("antiquewhite1",new Color(255,239,219));
    doAddColor("antiquewhite2",new Color(238,223,204));
    doAddColor("antiquewhite3",new Color(205,192,176));
    doAddColor("antiquewhite4",new Color(139,131,120));
    doAddColor("aquamarine",new Color(127,255,212));
    doAddColor("aquamarine1",new Color(127,255,212));
    doAddColor("aquamarine2",new Color(118,238,198));
    doAddColor("aquamarine3",new Color(102,205,170));
    doAddColor("aquamarine4",new Color(69,139,116));
    doAddColor("azure",new Color(240,255,255));
    doAddColor("azure1",new Color(240,255,255));
    doAddColor("azure2",new Color(224,238,238));
    doAddColor("azure3",new Color(193,205,205));
    doAddColor("azure4",new Color(131,139,139));
    doAddColor("beige",new Color(245,245,220));
    doAddColor("bisque",new Color(255,228,196));
    doAddColor("bisque1",new Color(255,228,196));
    doAddColor("bisque2",new Color(238,213,183));
    doAddColor("bisque3",new Color(205,183,158));
    doAddColor("bisque4",new Color(139,125,107));
    doAddColor("black",new Color(0,0,0));
    doAddColor("blanchedalmond",new Color(255,235,205));
    doAddColor("blue",new Color(0,0,255));
    doAddColor("blue1",new Color(0,0,255));
    doAddColor("blue2",new Color(0,0,238));
    doAddColor("blue3",new Color(0,0,205));
    doAddColor("blue4",new Color(0,0,139));
    doAddColor("blueviolet",new Color(138,43,226));
    doAddColor("brown",new Color(165,42,42));
    doAddColor("brown1",new Color(255,64,64));
    doAddColor("brown2",new Color(238,59,59));
    doAddColor("brown3",new Color(205,51,51));
    doAddColor("brown4",new Color(139,35,35));
    doAddColor("burlywood",new Color(222,184,135));
    doAddColor("burlywood1",new Color(255,211,155));
    doAddColor("burlywood2",new Color(238,197,145));
    doAddColor("burlywood3",new Color(205,170,125));
    doAddColor("burlywood4",new Color(139,115,85));
    doAddColor("cadetblue",new Color(95,158,160));
    doAddColor("cadetblue1",new Color(152,245,255));
    doAddColor("cadetblue2",new Color(142,229,238));
    doAddColor("cadetblue3",new Color(122,197,205));
    doAddColor("cadetblue4",new Color(83,134,139));
    doAddColor("chartreuse",new Color(127,255,0));
    doAddColor("chartreuse1",new Color(127,255,0));
    doAddColor("chartreuse2",new Color(118,238,0));
    doAddColor("chartreuse3",new Color(102,205,0));
    doAddColor("chartreuse4",new Color(69,139,0));
    doAddColor("chocolate",new Color(210,105,30));
    doAddColor("chocolate1",new Color(255,127,36));
    doAddColor("chocolate2",new Color(238,118,33));
    doAddColor("chocolate3",new Color(205,102,29));
    doAddColor("chocolate4",new Color(139,69,19));
    doAddColor("coral",new Color(255,127,80));
    doAddColor("coral1",new Color(255,114,86));
    doAddColor("coral2",new Color(238,106,80));
    doAddColor("coral3",new Color(205,91,69));
    doAddColor("coral4",new Color(139,62,47));
    doAddColor("cornflowerblue",new Color(100,149,237));
    doAddColor("cornsilk",new Color(255,248,220));
    doAddColor("cornsilk1",new Color(255,248,220));
    doAddColor("cornsilk2",new Color(238,232,205));
    doAddColor("cornsilk3",new Color(205,200,177));
    doAddColor("cornsilk4",new Color(139,136,120));
    doAddColor("crimson",new Color(220,20,60));
    doAddColor("cyan",new Color(0,255,255));
    doAddColor("cyan1",new Color(0,255,255));
    doAddColor("cyan2",new Color(0,238,238));
    doAddColor("cyan3",new Color(0,205,205));
    doAddColor("cyan4",new Color(0,139,139));
    doAddColor("darkblue",new Color(0,0,139));
    doAddColor("darkcyan",new Color(0,139,139));
    doAddColor("darkgoldenrod",new Color(184,134,11));
    doAddColor("darkgoldenrod1",new Color(255,185,15));
    doAddColor("darkgoldenrod2",new Color(238,173,14));
    doAddColor("darkgoldenrod3",new Color(205,149,12));
    doAddColor("darkgoldenrod4",new Color(139,101,8));
    doAddColor("darkgray",new Color(169,169,169));
    doAddColor("darkgreen",new Color(0,100,0));
    doAddColor("darkgrey",new Color(169,169,169));
    doAddColor("darkkhaki",new Color(189,183,107));
    doAddColor("darkmagenta",new Color(139,0,139));
    doAddColor("darkolivegreen",new Color(85,107,47));
    doAddColor("darkolivegreen1",new Color(202,255,112));
    doAddColor("darkolivegreen2",new Color(188,238,104));
    doAddColor("darkolivegreen3",new Color(162,205,90));
    doAddColor("darkolivegreen4",new Color(110,139,61));
    doAddColor("darkorange",new Color(255,140,0));
    doAddColor("darkorange1",new Color(255,127,0));
    doAddColor("darkorange2",new Color(238,118,0));
    doAddColor("darkorange3",new Color(205,102,0));
    doAddColor("darkorange4",new Color(139,69,0));
    doAddColor("darkorchid",new Color(153,50,204));
    doAddColor("darkorchid1",new Color(191,62,255));
    doAddColor("darkorchid2",new Color(178,58,238));
    doAddColor("darkorchid3",new Color(154,50,205));
    doAddColor("darkorchid4",new Color(104,34,139));
    doAddColor("darkred",new Color(139,0,0));
    doAddColor("darksalmon",new Color(233,150,122));
    doAddColor("darkseagreen",new Color(143,188,143));
    doAddColor("darkseagreen1",new Color(193,255,193));
    doAddColor("darkseagreen2",new Color(180,238,180));
    doAddColor("darkseagreen3",new Color(155,205,155));
    doAddColor("darkseagreen4",new Color(105,139,105));
    doAddColor("darkslateblue",new Color(72,61,139));
    doAddColor("darkslategray",new Color(47,79,79));
    doAddColor("darkslategray1",new Color(151,255,255));
    doAddColor("darkslategray2",new Color(141,238,238));
    doAddColor("darkslategray3",new Color(121,205,205));
    doAddColor("darkslategray4",new Color(82,139,139));
    doAddColor("darkslategrey",new Color(47,79,79));
    doAddColor("darkturquoise",new Color(0,206,209));
    doAddColor("darkviolet",new Color(148,0,211));
    doAddColor("deeppink",new Color(255,20,147));
    doAddColor("deeppink1",new Color(255,20,147));
    doAddColor("deeppink2",new Color(238,18,137));
    doAddColor("deeppink3",new Color(205,16,118));
    doAddColor("deeppink4",new Color(139,10,80));
    doAddColor("deepskyblue",new Color(0,191,255));
    doAddColor("deepskyblue1",new Color(0,191,255));
    doAddColor("deepskyblue2",new Color(0,178,238));
    doAddColor("deepskyblue3",new Color(0,154,205));
    doAddColor("deepskyblue4",new Color(0,104,139));
    doAddColor("dimgray",new Color(105,105,105));
    doAddColor("dimgrey",new Color(105,105,105));
    doAddColor("dodgerblue",new Color(30,144,255));
    doAddColor("dodgerblue1",new Color(30,144,255));
    doAddColor("dodgerblue2",new Color(28,134,238));
    doAddColor("dodgerblue3",new Color(24,116,205));
    doAddColor("dodgerblue4",new Color(16,78,139));
    doAddColor("firebrick",new Color(178,34,34));
    doAddColor("firebrick1",new Color(255,48,48));
    doAddColor("firebrick2",new Color(238,44,44));
    doAddColor("firebrick3",new Color(205,38,38));
    doAddColor("firebrick4",new Color(139,26,26));
    doAddColor("floralwhite",new Color(255,250,240));
    doAddColor("forestgreen",new Color(34,139,34));
    doAddColor("gainsboro",new Color(220,220,220));
    doAddColor("ghostwhite",new Color(248,248,255));
    doAddColor("gold",new Color(255,215,0));
    doAddColor("gold1",new Color(255,215,0));
    doAddColor("gold2",new Color(238,201,0));
    doAddColor("gold3",new Color(205,173,0));
    doAddColor("gold4",new Color(139,117,0));
    doAddColor("goldenrod",new Color(218,165,32));
    doAddColor("goldenrod1",new Color(255,193,37));
    doAddColor("goldenrod2",new Color(238,180,34));
    doAddColor("goldenrod3",new Color(205,155,29));
    doAddColor("goldenrod4",new Color(139,105,20));
    doAddColor("green",new Color(0,255,0));
    doAddColor("green1",new Color(0,255,0));
    doAddColor("green2",new Color(0,238,0));
    doAddColor("green3",new Color(0,205,0));
    doAddColor("green4",new Color(0,139,0));
    doAddColor("greenyellow",new Color(173,255,47));
    doAddColor("gray",new Color(190,190,190));
    doAddColor("grey",new Color(190,190,190));
    doAddColor("gray0",new Color(0,0,0));
    doAddColor("grey0",new Color(0,0,0));
    doAddColor("gray1",new Color(3,3,3));
    doAddColor("grey1",new Color(3,3,3));
    doAddColor("gray2",new Color(5,5,5));
    doAddColor("grey2",new Color(5,5,5));
    doAddColor("gray3",new Color(8,8,8));
    doAddColor("grey3",new Color(8,8,8));
    doAddColor("gray4",new Color(10,10,10));
    doAddColor("grey4",new Color(10,10,10));
    doAddColor("gray5",new Color(13,13,13));
    doAddColor("grey5",new Color(13,13,13));
    doAddColor("gray6",new Color(15,15,15));
    doAddColor("grey6",new Color(15,15,15));
    doAddColor("gray7",new Color(18,18,18));
    doAddColor("grey7",new Color(18,18,18));
    doAddColor("gray8",new Color(20,20,20));
    doAddColor("grey8",new Color(20,20,20));
    doAddColor("gray9",new Color(23,23,23));
    doAddColor("grey9",new Color(23,23,23));
    doAddColor("gray10",new Color(26,26,26));
    doAddColor("grey10",new Color(26,26,26));
    doAddColor("gray11",new Color(28,28,28));
    doAddColor("grey11",new Color(28,28,28));
    doAddColor("gray12",new Color(31,31,31));
    doAddColor("grey12",new Color(31,31,31));
    doAddColor("gray13",new Color(33,33,33));
    doAddColor("grey13",new Color(33,33,33));
    doAddColor("gray14",new Color(36,36,36));
    doAddColor("grey14",new Color(36,36,36));
    doAddColor("gray15",new Color(38,38,38));
    doAddColor("grey15",new Color(38,38,38));
    doAddColor("gray16",new Color(41,41,41));
    doAddColor("grey16",new Color(41,41,41));
    doAddColor("gray17",new Color(43,43,43));
    doAddColor("grey17",new Color(43,43,43));
    doAddColor("gray18",new Color(46,46,46));
    doAddColor("grey18",new Color(46,46,46));
    doAddColor("gray19",new Color(48,48,48));
    doAddColor("grey19",new Color(48,48,48));
    doAddColor("gray20",new Color(51,51,51));
    doAddColor("grey20",new Color(51,51,51));
    doAddColor("gray21",new Color(54,54,54));
    doAddColor("grey21",new Color(54,54,54));
    doAddColor("gray22",new Color(56,56,56));
    doAddColor("grey22",new Color(56,56,56));
    doAddColor("gray23",new Color(59,59,59));
    doAddColor("grey23",new Color(59,59,59));
    doAddColor("gray24",new Color(61,61,61));
    doAddColor("grey24",new Color(61,61,61));
    doAddColor("gray25",new Color(64,64,64));
    doAddColor("grey25",new Color(64,64,64));
    doAddColor("gray26",new Color(66,66,66));
    doAddColor("grey26",new Color(66,66,66));
    doAddColor("gray27",new Color(69,69,69));
    doAddColor("grey27",new Color(69,69,69));
    doAddColor("gray28",new Color(71,71,71));
    doAddColor("grey28",new Color(71,71,71));
    doAddColor("gray29",new Color(74,74,74));
    doAddColor("grey29",new Color(74,74,74));
    doAddColor("gray30",new Color(77,77,77));
    doAddColor("grey30",new Color(77,77,77));
    doAddColor("gray31",new Color(79,79,79));
    doAddColor("grey31",new Color(79,79,79));
    doAddColor("gray32",new Color(82,82,82));
    doAddColor("grey32",new Color(82,82,82));
    doAddColor("gray33",new Color(84,84,84));
    doAddColor("grey33",new Color(84,84,84));
    doAddColor("gray34",new Color(87,87,87));
    doAddColor("grey34",new Color(87,87,87));
    doAddColor("gray35",new Color(89,89,89));
    doAddColor("grey35",new Color(89,89,89));
    doAddColor("gray36",new Color(92,92,92));
    doAddColor("grey36",new Color(92,92,92));
    doAddColor("gray37",new Color(94,94,94));
    doAddColor("grey37",new Color(94,94,94));
    doAddColor("gray38",new Color(97,97,97));
    doAddColor("grey38",new Color(97,97,97));
    doAddColor("gray39",new Color(99,99,99));
    doAddColor("grey39",new Color(99,99,99));
    doAddColor("gray40",new Color(102,102,102));
    doAddColor("grey40",new Color(102,102,102));
    doAddColor("gray41",new Color(105,105,105));
    doAddColor("grey41",new Color(105,105,105));
    doAddColor("gray42",new Color(107,107,107));
    doAddColor("grey42",new Color(107,107,107));
    doAddColor("gray43",new Color(110,110,110));
    doAddColor("grey43",new Color(110,110,110));
    doAddColor("gray44",new Color(112,112,112));
    doAddColor("grey44",new Color(112,112,112));
    doAddColor("gray45",new Color(115,115,115));
    doAddColor("grey45",new Color(115,115,115));
    doAddColor("gray46",new Color(117,117,117));
    doAddColor("grey46",new Color(117,117,117));
    doAddColor("gray47",new Color(120,120,120));
    doAddColor("grey47",new Color(120,120,120));
    doAddColor("gray48",new Color(122,122,122));
    doAddColor("grey48",new Color(122,122,122));
    doAddColor("gray49",new Color(125,125,125));
    doAddColor("grey49",new Color(125,125,125));
    doAddColor("gray50",new Color(127,127,127));
    doAddColor("grey50",new Color(127,127,127));
    doAddColor("gray51",new Color(130,130,130));
    doAddColor("grey51",new Color(130,130,130));
    doAddColor("gray52",new Color(133,133,133));
    doAddColor("grey52",new Color(133,133,133));
    doAddColor("gray53",new Color(135,135,135));
    doAddColor("grey53",new Color(135,135,135));
    doAddColor("gray54",new Color(138,138,138));
    doAddColor("grey54",new Color(138,138,138));
    doAddColor("gray55",new Color(140,140,140));
    doAddColor("grey55",new Color(140,140,140));
    doAddColor("gray56",new Color(143,143,143));
    doAddColor("grey56",new Color(143,143,143));
    doAddColor("gray57",new Color(145,145,145));
    doAddColor("grey57",new Color(145,145,145));
    doAddColor("gray58",new Color(148,148,148));
    doAddColor("grey58",new Color(148,148,148));
    doAddColor("gray59",new Color(150,150,150));
    doAddColor("grey59",new Color(150,150,150));
    doAddColor("gray60",new Color(153,153,153));
    doAddColor("grey60",new Color(153,153,153));
    doAddColor("gray61",new Color(156,156,156));
    doAddColor("grey61",new Color(156,156,156));
    doAddColor("gray62",new Color(158,158,158));
    doAddColor("grey62",new Color(158,158,158));
    doAddColor("gray63",new Color(161,161,161));
    doAddColor("grey63",new Color(161,161,161));
    doAddColor("gray64",new Color(163,163,163));
    doAddColor("grey64",new Color(163,163,163));
    doAddColor("gray65",new Color(166,166,166));
    doAddColor("grey65",new Color(166,166,166));
    doAddColor("gray66",new Color(168,168,168));
    doAddColor("grey66",new Color(168,168,168));
    doAddColor("gray67",new Color(171,171,171));
    doAddColor("grey67",new Color(171,171,171));
    doAddColor("gray68",new Color(173,173,173));
    doAddColor("grey68",new Color(173,173,173));
    doAddColor("gray69",new Color(176,176,176));
    doAddColor("grey69",new Color(176,176,176));
    doAddColor("gray70",new Color(179,179,179));
    doAddColor("grey70",new Color(179,179,179));
    doAddColor("gray71",new Color(181,181,181));
    doAddColor("grey71",new Color(181,181,181));
    doAddColor("gray72",new Color(184,184,184));
    doAddColor("grey72",new Color(184,184,184));
    doAddColor("gray73",new Color(186,186,186));
    doAddColor("grey73",new Color(186,186,186));
    doAddColor("gray74",new Color(189,189,189));
    doAddColor("grey74",new Color(189,189,189));
    doAddColor("gray75",new Color(191,191,191));
    doAddColor("grey75",new Color(191,191,191));
    doAddColor("gray76",new Color(194,194,194));
    doAddColor("grey76",new Color(194,194,194));
    doAddColor("gray77",new Color(196,196,196));
    doAddColor("grey77",new Color(196,196,196));
    doAddColor("gray78",new Color(199,199,199));
    doAddColor("grey78",new Color(199,199,199));
    doAddColor("gray79",new Color(201,201,201));
    doAddColor("grey79",new Color(201,201,201));
    doAddColor("gray80",new Color(204,204,204));
    doAddColor("grey80",new Color(204,204,204));
    doAddColor("gray81",new Color(207,207,207));
    doAddColor("grey81",new Color(207,207,207));
    doAddColor("gray82",new Color(209,209,209));
    doAddColor("grey82",new Color(209,209,209));
    doAddColor("gray83",new Color(212,212,212));
    doAddColor("grey83",new Color(212,212,212));
    doAddColor("gray84",new Color(214,214,214));
    doAddColor("grey84",new Color(214,214,214));
    doAddColor("gray85",new Color(217,217,217));
    doAddColor("grey85",new Color(217,217,217));
    doAddColor("gray86",new Color(219,219,219));
    doAddColor("grey86",new Color(219,219,219));
    doAddColor("gray87",new Color(222,222,222));
    doAddColor("grey87",new Color(222,222,222));
    doAddColor("gray88",new Color(224,224,224));
    doAddColor("grey88",new Color(224,224,224));
    doAddColor("gray89",new Color(227,227,227));
    doAddColor("grey89",new Color(227,227,227));
    doAddColor("gray90",new Color(229,229,229));
    doAddColor("grey90",new Color(229,229,229));
    doAddColor("gray91",new Color(232,232,232));
    doAddColor("grey91",new Color(232,232,232));
    doAddColor("gray92",new Color(235,235,235));
    doAddColor("grey92",new Color(235,235,235));
    doAddColor("gray93",new Color(237,237,237));
    doAddColor("grey93",new Color(237,237,237));
    doAddColor("gray94",new Color(240,240,240));
    doAddColor("grey94",new Color(240,240,240));
    doAddColor("gray95",new Color(242,242,242));
    doAddColor("grey95",new Color(242,242,242));
    doAddColor("gray96",new Color(245,245,245));
    doAddColor("grey96",new Color(245,245,245));
    doAddColor("gray97",new Color(247,247,247));
    doAddColor("grey97",new Color(247,247,247));
    doAddColor("gray98",new Color(250,250,250));
    doAddColor("grey98",new Color(250,250,250));
    doAddColor("gray99",new Color(252,252,252));
    doAddColor("grey99",new Color(252,252,252));
    doAddColor("gray100",new Color(255,255,255));
    doAddColor("grey100",new Color(255,255,255));
    doAddColor("honeydew",new Color(240,255,240));
    doAddColor("honeydew1",new Color(240,255,240));
    doAddColor("honeydew2",new Color(224,238,224));
    doAddColor("honeydew3",new Color(193,205,193));
    doAddColor("honeydew4",new Color(131,139,131));
    doAddColor("hotpink",new Color(255,105,180));
    doAddColor("hotpink1",new Color(255,110,180));
    doAddColor("hotpink2",new Color(238,106,167));
    doAddColor("hotpink3",new Color(205,96,144));
    doAddColor("hotpink4",new Color(139,58,98));
    doAddColor("indianred",new Color(205,92,92));
    doAddColor("indianred1",new Color(255,106,106));
    doAddColor("indianred2",new Color(238,99,99));
    doAddColor("indianred3",new Color(205,85,85));
    doAddColor("indianred4",new Color(139,58,58));
    doAddColor("indigo",new Color(75,0,130));
    doAddColor("ivory",new Color(255,255,240));
    doAddColor("ivory1",new Color(255,255,240));
    doAddColor("ivory2",new Color(238,238,224));
    doAddColor("ivory3",new Color(205,205,193));
    doAddColor("ivory4",new Color(139,139,131));
    doAddColor("khaki",new Color(240,230,140));
    doAddColor("khaki1",new Color(255,246,143));
    doAddColor("khaki2",new Color(238,230,133));
    doAddColor("khaki3",new Color(205,198,115));
    doAddColor("khaki4",new Color(139,134,78));
    doAddColor("lavender",new Color(230,230,250));
    doAddColor("lavenderblush",new Color(255,240,245));
    doAddColor("lavenderblush1",new Color(255,240,245));
    doAddColor("lavenderblush2",new Color(238,224,229));
    doAddColor("lavenderblush3",new Color(205,193,197));
    doAddColor("lavenderblush4",new Color(139,131,134));
    doAddColor("lawngreen",new Color(124,252,0));
    doAddColor("lemonchiffon",new Color(255,250,205));
    doAddColor("lemonchiffon1",new Color(255,250,205));
    doAddColor("lemonchiffon2",new Color(238,233,191));
    doAddColor("lemonchiffon3",new Color(205,201,165));
    doAddColor("lemonchiffon4",new Color(139,137,112));
    doAddColor("lightblue",new Color(173,216,230));
    doAddColor("lightblue1",new Color(191,239,255));
    doAddColor("lightblue2",new Color(178,223,238));
    doAddColor("lightblue3",new Color(154,192,205));
    doAddColor("lightblue4",new Color(104,131,139));
    doAddColor("lightcoral",new Color(240,128,128));
    doAddColor("lightcyan",new Color(224,255,255));
    doAddColor("lightcyan1",new Color(224,255,255));
    doAddColor("lightcyan2",new Color(209,238,238));
    doAddColor("lightcyan3",new Color(180,205,205));
    doAddColor("lightcyan4",new Color(122,139,139));
    doAddColor("lightgoldenrod",new Color(238,221,130));
    doAddColor("lightgoldenrod1",new Color(255,236,139));
    doAddColor("lightgoldenrod2",new Color(238,220,130));
    doAddColor("lightgoldenrod3",new Color(205,190,112));
    doAddColor("lightgoldenrod4",new Color(139,129,76));
    doAddColor("lightgoldenrodyellow",new Color(250,250,210));
    doAddColor("lightgray",new Color(211,211,211));
    doAddColor("lightgreen",new Color(144,238,144));
    doAddColor("lightgrey",new Color(211,211,211));
    doAddColor("lightpink",new Color(255,182,193));
    doAddColor("lightpink1",new Color(255,174,185));
    doAddColor("lightpink2",new Color(238,162,173));
    doAddColor("lightpink3",new Color(205,140,149));
    doAddColor("lightpink4",new Color(139,95,101));
    doAddColor("lightsalmon",new Color(255,160,122));
    doAddColor("lightsalmon1",new Color(255,160,122));
    doAddColor("lightsalmon2",new Color(238,149,114));
    doAddColor("lightsalmon3",new Color(205,129,98));
    doAddColor("lightsalmon4",new Color(139,87,66));
    doAddColor("lightseagreen",new Color(32,178,170));
    doAddColor("lightskyblue",new Color(135,206,250));
    doAddColor("lightskyblue1",new Color(176,226,255));
    doAddColor("lightskyblue2",new Color(164,211,238));
    doAddColor("lightskyblue3",new Color(141,182,205));
    doAddColor("lightskyblue4",new Color(96,123,139));
    doAddColor("lightslateblue",new Color(132,112,255));
    doAddColor("lightslategray",new Color(119,136,153));
    doAddColor("lightslategrey",new Color(119,136,153));
    doAddColor("lightsteelblue",new Color(176,196,222));
    doAddColor("lightsteelblue1",new Color(202,225,255));
    doAddColor("lightsteelblue2",new Color(188,210,238));
    doAddColor("lightsteelblue3",new Color(162,181,205));
    doAddColor("lightsteelblue4",new Color(110,123,139));
    doAddColor("lightyellow",new Color(255,255,224));
    doAddColor("lightyellow1",new Color(255,255,224));
    doAddColor("lightyellow2",new Color(238,238,209));
    doAddColor("lightyellow3",new Color(205,205,180));
    doAddColor("lightyellow4",new Color(139,139,122));
    doAddColor("limegreen",new Color(50,205,50));
    doAddColor("linen",new Color(250,240,230));
    doAddColor("magenta",new Color(255,0,255));
    doAddColor("magenta1",new Color(255,0,255));
    doAddColor("magenta2",new Color(238,0,238));
    doAddColor("magenta3",new Color(205,0,205));
    doAddColor("magenta4",new Color(139,0,139));
    doAddColor("maroon",new Color(176,48,96));
    doAddColor("maroon1",new Color(255,52,179));
    doAddColor("maroon2",new Color(238,48,167));
    doAddColor("maroon3",new Color(205,41,144));
    doAddColor("maroon4",new Color(139,28,98));
    doAddColor("mediumaquamarine",new Color(102,205,170));
    doAddColor("mediumblue",new Color(0,0,205));
    doAddColor("mediumorchid",new Color(186,85,211));
    doAddColor("mediumorchid1",new Color(224,102,255));
    doAddColor("mediumorchid2",new Color(209,95,238));
    doAddColor("mediumorchid3",new Color(180,82,205));
    doAddColor("mediumorchid4",new Color(122,55,139));
    doAddColor("mediumpurple",new Color(147,112,219));
    doAddColor("mediumpurple1",new Color(171,130,255));
    doAddColor("mediumpurple2",new Color(159,121,238));
    doAddColor("mediumpurple3",new Color(137,104,205));
    doAddColor("mediumpurple4",new Color(93,71,139));
    doAddColor("mediumseagreen",new Color(60,179,113));
    doAddColor("mediumslateblue",new Color(123,104,238));
    doAddColor("mediumspringgreen",new Color(0,250,154));
    doAddColor("mediumturquoise",new Color(72,209,204));
    doAddColor("mediumvioletred",new Color(199,21,133));
    doAddColor("midnightblue",new Color(25,25,112));
    doAddColor("mintcream",new Color(245,255,250));
    doAddColor("mistyrose",new Color(255,228,225));
    doAddColor("mistyrose1",new Color(255,228,225));
    doAddColor("mistyrose2",new Color(238,213,210));
    doAddColor("mistyrose3",new Color(205,183,181));
    doAddColor("mistyrose4",new Color(139,125,123));
    doAddColor("moccasin",new Color(255,228,181));
    doAddColor("navajowhite",new Color(255,222,173));
    doAddColor("navajowhite1",new Color(255,222,173));
    doAddColor("navajowhite2",new Color(238,207,161));
    doAddColor("navajowhite3",new Color(205,179,139));
    doAddColor("navajowhite4",new Color(139,121,94));
    doAddColor("navy",new Color(0,0,128));
    doAddColor("navyblue",new Color(0,0,128));
    doAddColor("oldlace",new Color(253,245,230));
    doAddColor("olivedrab",new Color(107,142,35));
    doAddColor("olivedrab1",new Color(192,255,62));
    doAddColor("olivedrab2",new Color(179,238,58));
    doAddColor("olivedrab3",new Color(154,205,50));
    doAddColor("olivedrab4",new Color(105,139,34));
    doAddColor("orange",new Color(255,165,0));
    doAddColor("orange1",new Color(255,165,0));
    doAddColor("orange2",new Color(238,154,0));
    doAddColor("orange3",new Color(205,133,0));
    doAddColor("orange4",new Color(139,90,0));
    doAddColor("orangered",new Color(255,69,0));
    doAddColor("orangered1",new Color(255,69,0));
    doAddColor("orangered2",new Color(238,64,0));
    doAddColor("orangered3",new Color(205,55,0));
    doAddColor("orangered4",new Color(139,37,0));
    doAddColor("orchid",new Color(218,112,214));
    doAddColor("orchid1",new Color(255,131,250));
    doAddColor("orchid2",new Color(238,122,233));
    doAddColor("orchid3",new Color(205,105,201));
    doAddColor("orchid4",new Color(139,71,137));
    doAddColor("palegoldenrod",new Color(238,232,170));
    doAddColor("palegreen",new Color(152,251,152));
    doAddColor("palegreen1",new Color(154,255,154));
    doAddColor("palegreen2",new Color(144,238,144));
    doAddColor("palegreen3",new Color(124,205,124));
    doAddColor("palegreen4",new Color(84,139,84));
    doAddColor("paleturquoise",new Color(175,238,238));
    doAddColor("paleturquoise1",new Color(187,255,255));
    doAddColor("paleturquoise2",new Color(174,238,238));
    doAddColor("paleturquoise3",new Color(150,205,205));
    doAddColor("paleturquoise4",new Color(102,139,139));
    doAddColor("palevioletred",new Color(219,112,147));
    doAddColor("palevioletred1",new Color(255,130,171));
    doAddColor("palevioletred2",new Color(238,121,159));
    doAddColor("palevioletred3",new Color(205,104,137));
    doAddColor("palevioletred4",new Color(139,71,93));
    doAddColor("papayawhip",new Color(255,239,213));
    doAddColor("peachpuff",new Color(255,218,185));
    doAddColor("peachpuff1",new Color(255,218,185));
    doAddColor("peachpuff2",new Color(238,203,173));
    doAddColor("peachpuff3",new Color(205,175,149));
    doAddColor("peachpuff4",new Color(139,119,101));
    doAddColor("peru",new Color(205,133,63));
    doAddColor("pink",new Color(255,192,203));
    doAddColor("pink1",new Color(255,181,197));
    doAddColor("pink2",new Color(238,169,184));
    doAddColor("pink3",new Color(205,145,158));
    doAddColor("pink4",new Color(139,99,108));
    doAddColor("plum",new Color(221,160,221));
    doAddColor("plum1",new Color(255,187,255));
    doAddColor("plum2",new Color(238,174,238));
    doAddColor("plum3",new Color(205,150,205));
    doAddColor("plum4",new Color(139,102,139));
    doAddColor("powderblue",new Color(176,224,230));
    doAddColor("purple",new Color(160,32,240));
    doAddColor("purple1",new Color(155,48,255));
    doAddColor("purple2",new Color(145,44,238));
    doAddColor("purple3",new Color(125,38,205));
    doAddColor("purple4",new Color(85,26,139));
    doAddColor("red",new Color(255,0,0));
    doAddColor("red1",new Color(255,0,0));
    doAddColor("red2",new Color(238,0,0));
    doAddColor("red3",new Color(205,0,0));
    doAddColor("red4",new Color(139,0,0));
    doAddColor("rosybrown",new Color(188,143,143));
    doAddColor("rosybrown1",new Color(255,193,193));
    doAddColor("rosybrown2",new Color(238,180,180));
    doAddColor("rosybrown3",new Color(205,155,155));
    doAddColor("rosybrown4",new Color(139,105,105));
    doAddColor("royalblue",new Color(65,105,225));
    doAddColor("royalblue1",new Color(72,118,255));
    doAddColor("royalblue2",new Color(67,110,238));
    doAddColor("royalblue3",new Color(58,95,205));
    doAddColor("royalblue4",new Color(39,64,139));
    doAddColor("saddlebrown",new Color(139,69,19));
    doAddColor("salmon",new Color(250,128,114));
    doAddColor("salmon1",new Color(255,140,105));
    doAddColor("salmon2",new Color(238,130,98));
    doAddColor("salmon3",new Color(205,112,84));
    doAddColor("salmon4",new Color(139,76,57));
    doAddColor("sandybrown",new Color(244,164,96));
    doAddColor("seagreen",new Color(46,139,87));
    doAddColor("seagreen1",new Color(84,255,159));
    doAddColor("seagreen2",new Color(78,238,148));
    doAddColor("seagreen3",new Color(67,205,128));
    doAddColor("seagreen4",new Color(46,139,87));
    doAddColor("seashell",new Color(255,245,238));
    doAddColor("seashell1",new Color(255,245,238));
    doAddColor("seashell2",new Color(238,229,222));
    doAddColor("seashell3",new Color(205,197,191));
    doAddColor("seashell4",new Color(139,134,130));
    doAddColor("sgiindigo2",new Color(33,136,104));
    doAddColor("sienna",new Color(160,82,45));
    doAddColor("sienna1",new Color(255,130,71));
    doAddColor("sienna2",new Color(238,121,66));
    doAddColor("sienna3",new Color(205,104,57));
    doAddColor("sienna4",new Color(139,71,38));
    doAddColor("skyblue",new Color(135,206,235));
    doAddColor("skyblue1",new Color(135,206,255));
    doAddColor("skyblue2",new Color(126,192,238));
    doAddColor("skyblue3",new Color(108,166,205));
    doAddColor("skyblue4",new Color(74,112,139));
    doAddColor("slateblue",new Color(106,90,205));
    doAddColor("slateblue1",new Color(131,111,255));
    doAddColor("slateblue2",new Color(122,103,238));
    doAddColor("slateblue3",new Color(105,89,205));
    doAddColor("slateblue4",new Color(71,60,139));
    doAddColor("slategray",new Color(112,128,144));
    doAddColor("slategray1",new Color(198,226,255));
    doAddColor("slategray2",new Color(185,211,238));
    doAddColor("slategray3",new Color(159,182,205));
    doAddColor("slategray4",new Color(108,123,139));
    doAddColor("slategrey",new Color(112,128,144));
    doAddColor("snow",new Color(255,250,250));
    doAddColor("snow1",new Color(255,250,250));
    doAddColor("snow2",new Color(238,233,233));
    doAddColor("snow3",new Color(205,201,201));
    doAddColor("snow4",new Color(139,137,137));
    doAddColor("springgreen",new Color(0,255,127));
    doAddColor("springgreen1",new Color(0,255,127));
    doAddColor("springgreen2",new Color(0,238,118));
    doAddColor("springgreen3",new Color(0,205,102));
    doAddColor("springgreen4",new Color(0,139,69));
    doAddColor("steelblue",new Color(70,130,180));
    doAddColor("steelblue1",new Color(99,184,255));
    doAddColor("steelblue2",new Color(92,172,238));
    doAddColor("steelblue3",new Color(79,148,205));
    doAddColor("steelblue4",new Color(54,100,139));
    doAddColor("tan",new Color(210,180,140));
    doAddColor("tan1",new Color(255,165,79));
    doAddColor("tan2",new Color(238,154,73));
    doAddColor("tan3",new Color(205,133,63));
    doAddColor("tan4",new Color(139,90,43));
    doAddColor("thistle",new Color(216,191,216));
    doAddColor("thistle1",new Color(255,225,255));
    doAddColor("thistle2",new Color(238,210,238));
    doAddColor("thistle3",new Color(205,181,205));
    doAddColor("thistle4",new Color(139,123,139));
    doAddColor("tomato",new Color(255,99,71));
    doAddColor("tomato1",new Color(255,99,71));
    doAddColor("tomato2",new Color(238,92,66));
    doAddColor("tomato3",new Color(205,79,57));
    doAddColor("tomato4",new Color(139,54,38));
    doAddColor("turquoise",new Color(64,224,208));
    doAddColor("turquoise1",new Color(0,245,255));
    doAddColor("turquoise2",new Color(0,229,238));
    doAddColor("turquoise3",new Color(0,197,205));
    doAddColor("turquoise4",new Color(0,134,139));
    doAddColor("violet",new Color(238,130,238));
    doAddColor("violetred",new Color(208,32,144));
    doAddColor("violetred1",new Color(255,62,150));
    doAddColor("violetred2",new Color(238,58,140));
    doAddColor("violetred3",new Color(205,50,120));
    doAddColor("violetred4",new Color(139,34,82));
    doAddColor("wheat",new Color(245,222,179));
    doAddColor("wheat1",new Color(255,231,186));
    doAddColor("wheat2",new Color(238,216,174));
    doAddColor("wheat3",new Color(205,186,150));
    doAddColor("wheat4",new Color(139,126,102));
    doAddColor("white",new Color(255,255,255));
    doAddColor("whitesmoke",new Color(245,245,245));
    doAddColor("yellow",new Color(255,255,0));
    doAddColor("yellow1",new Color(255,255,0));
    doAddColor("yellow2",new Color(238,238,0));
    doAddColor("yellow3",new Color(205,205,0));
    doAddColor("yellow4",new Color(139,139,0));
    doAddColor("yellowgreen",new Color(154,205,50));

    Toolkit tk = Toolkit.getDefaultToolkit();
    String[] fonts = tk.getFontList();
    for(int i = 0; i < fonts.length; i++) {
      fontLookUp.put(canonColor(fonts[i]),fonts[i]);
    }
  }

  // be sure to specify colors that exist initially in the colorTable
  public static final Color  defaultForeground = getColor("black",null);
  public static final Color  defaultBackground = getColor("white",null);
  public static final Color  defaultXOR        = getColor("light gray",null);
  public static final Color  defaultFontcolor  = getColor("black",null);
  public static final Color  defaultColor      = getColor("black",null);

  public static final int    defaultFontstyle  = Font.PLAIN;
  public static final int    defaultFontsize   = 14;
  public static final String defaultFontname   = "TimesRoman";
  public static final Font   defaultFont       = new Font(defaultFontname,defaultFontstyle,defaultFontsize);

  private Color       foregroundColor = defaultForeground;
  private Color       backgroundColor = defaultBackground;
  private Color       xorColor        = defaultXOR;
  private Color       fontcolorColor  = defaultFontcolor;
  private int         fontstyle       = defaultFontstyle;
  private int         fontsize        = defaultFontsize;
  private String      fontname        = defaultFontname;
  private Font        font            = defaultFont;
  private boolean     xorMode         = false;
  private MyRectangle   clipRect        = null;
  private boolean     fillMode        = false;
  private int         lineStyle       = Grappa.DOT_LINE_SOLID;

  private boolean setFontFlag = false;

  /**
   * Adds a color to the color table. For search purposes, names are
   * canonicalized by converting to lower case and stripping
   * non-alphanumerics.  A name must contains at least one alphabetic.
   * Once in the table, colors can be set by name, and names can be
   * retrieved by color.
   *
   * @param name the name to be used to reference the color.
   * @param color the Color value.
   */
  public static void addColor(String name, Color color) throws IllegalArgumentException {
    if(name == null || color == null) {
      throw new IllegalArgumentException("supplied name or color is null");
    }
    String canonName = canonColor(name);
    if(canonName == null) {
      throw new IllegalArgumentException("supplied name does not contain alphabetics (" + name + ")");
    }
    doAddColor(canonName,color);
  }

  // performs actual color table puts
  private static void doAddColor(String name, Color color) {
    colorTable.put(name,color);
    colorLookUp.put(color,name);
  }

  // canonicalizes color string (removes non-alphanumeric and lowers case)
  private static String canonColor(String name) {
    if(name == null) return null;
    char[] array = name.toCharArray();
    int len = 0;
    boolean allDigits = true;
    for(int i = 0; i < array.length; i++) {
      if(Character.isUpperCase(array[i])) {
	array[len++] = Character.toLowerCase(array[i]);
	allDigits = false;
      } else if(Character.isLowerCase(array[i])) {
	array[len++] = array[i];
	allDigits = false;
      } else if(Character.isDigit(array[i])) {
	array[len++] = array[i];
      }
    }
    if(len == 0 || allDigits) return null;
    return new String(array,0,len);
  }

  /**
   * Return the color in the color table with the given name.
   * If the color is not found, the supplied default is returned.
   * If the supplied default is null, the class default is returned.
   * If the name consists of three comma or space separated floating
   * point numbers in the range 0 to 1 inclusive, then it is assumed
   * to represent an HSB color specification and generated directly.
   * The name search is case insensitive and looks at alphanumerics only.
   *
   * @param name the name of the color to be retrieved.
   * @param color the color value to return if requested color
   *              is not found.
   *
   * @return the color matching the name or the default.
   */
  public static Color getColor(String name, Color color) {
    if(color == null) color = defaultColor;
    
    if(name == null) return color;

    String canonName = canonColor(name);

    if(canonName == null) {
      String trimmed = name.trim();
      char c = trimmed.charAt(0);
      if(trimmed.length() > 4 && (c == '0' || c == '1' || c == '.')) {
	// check if HSB
	float[] hsb = new float[3];
	int elem = 0;
	int start = 0;
	int end = 0;
	int len = trimmed.length();
	while(elem < 3 && end < len) {
	  while(end < len && (c = trimmed.charAt(end)) != ' ' && c != ',') {
	    end++;
	  }
	  if(end == len && elem < 2) {
	    break;
	  }
	  try {
	    hsb[elem] = Float.valueOf(trimmed.substring(start,end)).floatValue();
	  } catch(NumberFormatException nfe) {
	    break;
	  }
	  if(hsb[elem] < 0 || hsb[elem] > 1) {
	    break;
	  }
	  if(end == len && elem == 2) {
	    // finally, success!
	    return new Color(Color.HSBtoRGB(hsb[0],hsb[1],hsb[2]));
	  }
	  end++;
	  start = end;
	  elem++;
	}
      }
      return color;
    }
    
    Color retColor = (Color)colorTable.get(canonName);

    if(retColor == null) {
      retColor = color;
    }

    return retColor;
  }

  /**
   * Return the name of the supplied color.
   *
   * @param color the color whose name is to be retrieved.
   *
   * @return the color's name, if it is in the color table, or null.
   */
  public static String getColorName(Color color) {
    return((String)colorLookUp.get(color));
  }

  public GraphicContext() {
    //super();
  }

  /**
   * Set the foreground color to the named color.
   *
   * @param color the name of the color to be used as the foreground color.
   *
   * @return the previous value of the foreground color.
   */
  public Color setForeground(String color) {
    Color oldColor = foregroundColor;

    foregroundColor = getColor(color,defaultForeground);
    return oldColor;
  }

  /**
   * Set the foreground color to the supplied color.
   *
   * @param color the value of the color to be used as the foreground color.
   *
   * @return the previous value of the foreground color.
   */
  public Color setForeground(Color color) {
    Color oldColor = foregroundColor;

    if(color == null) color = defaultForeground;
    foregroundColor = color;
    return oldColor;
  }

  /**
   * Set the background color to the named color.
   *
   * @param color the name of the color to be used as the background color.
   *
   * @return the previous value of the background color.
   */
  public Color setBackground(String color) {
    Color oldColor = backgroundColor;

    backgroundColor = getColor(color,defaultBackground);
    return oldColor;
  }

  /**
   * Set the background color to the supplied color.
   *
   * @param color the value of the color to be used as the background color.
   *
   * @return the previous value of the background color.
   */
  public Color setBackground(Color color) {
    Color oldColor = backgroundColor;

    if(color == null) color = defaultBackground;
    backgroundColor = color;
    return oldColor;
  }

  /**
   * Set the x-or color to the named color.
   *
   * @param color the name of the color to be used as the x-or color.
   *
   * @return the previous value of the x-or color.
   */
  public Color setXORColor(String color) {
    Color oldColor = xorColor;

    xorColor = getColor(color,defaultXOR);
    return oldColor;
  }

  /**
   * Set the x-or color to the specified color.
   *
   * @param color the value of the color to be used as the x-or color.
   *
   * @return the previous value of the x-or color.
   */
  public Color setXORColor(Color color) {
    Color oldColor = xorColor;

    if(color == null) color = defaultXOR;
    xorColor = color;
    return oldColor;
  }

  /**
   * Set the font color to the named color.
   *
   * @param color the name of the color to be used as the font color.
   *
   * @return the previous value of the font color.
   */
  public Color setFontcolor(String color) {
    Color oldColor = fontcolorColor;

    fontcolorColor = getColor(color,defaultFontcolor);
    return oldColor;
  }

  /**
   * Set the font color to the specified color.
   *
   * @param color the value of the color to be used as the font color.
   *
   * @return the previous value of the font color.
   */
  public Color setFontcolor(Color color) {
    Color oldColor = fontcolorColor;

    if(color == null) color = defaultFontcolor;
    fontcolorColor = color;
    return oldColor;
  }

  /**
   * Set the font name. A call to setFont() is needed to actually
   * change the font setting.
   *
   * @param name the new font name.
   * @return the previous font name.
   * @see GraphicContext#setFont()
   */
  public String setFontname(String name) {
    String oldName = fontname;
    if(name == null || name.length() == 0) {
      name = defaultFontname;
    }
    // canonicalize as with colors
    fontname = (String)fontLookUp.get(canonColor(name));
    if(fontname == null) fontname = defaultFontname;
    if(!fontname.equals(oldName)) setFontFlag = true;
    return oldName;
  }

  /**
   * Set the font size. A call to setFont() is needed to actually
   * change the font setting.
   *
   * @param size the new font size.
   * @return the previous font size.
   * @see GraphicContext#setFont()
   */
  public int setFontsize(String size) {
    if(size == null) {
      throw new IllegalArgumentException("cannot call setFontsize with a null String");
    }
    int sz;
    try {
      sz = Integer.valueOf(size).intValue();
    } catch(NumberFormatException nfe) {
      throw new IllegalArgumentException("string argument to setFontsize must have proper integer format");
    }
    return(setFontsize(sz));
  }

  /**
   * Set the font size. A call to setFont() is needed to actually
   * change the font setting.
   *
   * @param size the new font size.
   * @return the previous font size.
   * @see GraphicContext#setFont()
   */
  public int setFontsize(int size) {
    int oldSize = fontsize;
    if(size <= 0) {
      size = defaultFontsize;
    }
    fontsize = size;
    if(fontsize != oldSize) setFontFlag = true;
    return oldSize;
  }

  /**
   * Set the font style. A call to setFont() is needed to actually
   * change the font setting.
   *
   * @param style the new font style.
   * @return the previous font style.
   * @see GraphicContext#setFont()
   * @see Font#PLAIN
   * @see Font#BOLD
   * @see Font#ITALIC
   */
  public int setFontstyle(int style) {
    int oldStyle = fontstyle;
    switch(style) {
    case Font.PLAIN:
    case Font.BOLD:
    case Font.ITALIC:
    case (Font.BOLD + Font.ITALIC):
      fontstyle = style;
      break;
    default:
      fontstyle = defaultFontstyle;
      break;
    }
    if(fontstyle != oldStyle) setFontFlag = true;
    return oldStyle;
  }

  /**
   * Set the font as specified. The specification was set by calls to
   * setFontname, setFontsize and setFontstyle.
   *
   * @return the previous font value.
   * @see GraphicContext#setFontname()
   * @see GraphicContext#setFontsize()
   * @see GraphicContext#setFontstyle()
   */
  public Font setFont() {
    Font oldFont = font;

    font = new Font(fontname,fontstyle,fontsize);

    if(font == null) {
      font = defaultFont;
    }

    fontname = font.getName();
    fontstyle = font.getStyle();
    fontsize = font.getSize();

    setFontFlag = false;

    return oldFont;
  }

  /**
   * Set the font to the supplied font.
   *
   * @param font the new font value to use.
   *
   * @return the previous font value.
   */
  public Font setFont(Font newFont) {
    Font oldFont = font;

    font = newFont;

    if(font == null) font = defaultFont;

    fontname = font.getName();
    fontstyle = font.getStyle();
    fontsize = font.getSize();

    setFontFlag = false;

    return oldFont;
  }

  /**
   * Set the x-or mode as specified.
   *
   * @param mode the new x-or mode value.
   *
   * @return the previous x-or mode value;
   */
  public boolean setXORMode(boolean mode) {
    boolean oldMode = xorMode;
    xorMode = mode;
    return oldMode;
  }

  /**
   * Set the clipping rectangle as specified.
   *
   * @param x the x coordinate.
   * @param y the y coordinate.
   * @param width the width of the rectangle.
   * @param height the height of the rectangle.
   *
   * @return the previous value of the clipping rectangle.
   */
  public MyRectangle setClipRect(int x, int y, int width, int height) {
    MyRectangle oldClipRect = clipRect;

    if(clipRect == null) {
      clipRect = new MyRectangle(x,y,width,height);
    } else {
      clipRect.setBounds(x,y,width,height);
    }

    return oldClipRect;
  }

  /**
   * Set the clipping rectangle to the specified value.
   *
   * @param rect the new clipping rectangle to use.
   *
   * @return the previous value of the clipping rectangle.
   */
  public MyRectangle setClipRect(MyRectangle rect) {
    if(rect == null) {
      MyRectangle oldClipRect = clipRect;
      clipRect = null;
      return oldClipRect;
    }
    
    return(setClipRect(rect.x,rect.y,rect.width,rect.height));
  }

  /**
   * Set the fill mode as specified.
   *
   * @param mode the new fill mode value.
   *
   * @return the previous fill mode value;
   */
  public boolean setFillMode(boolean mode) {
    boolean oldMode = fillMode;
    fillMode = mode;
    return oldMode;
  }

  /**
   * Set the line style as specified.
   *
   * @param style the new line style value.
   *
   * @return the previous line style value;
   */
  public int setLineStyle(int style) {
    int oldStyle = lineStyle;
    switch(style) {
    case Grappa.DOT_LINE_SOLID:
    case Grappa.DOT_LINE_DASHED:
    case Grappa.DOT_LINE_DOTTED:
      lineStyle = style;
      break;
    }
    return oldStyle;
  }

  /**
   * Set the line style as specified.
   *
   * @param style the new line style value.
   *
   * @return the previous line style value;
   */
  public String setLineStyle(String style) {
    String oldStyle = getLineStyleString();
    
    if(style == null) return oldStyle;
    if(style.equals(Grappa.DOT_LINE_SOLID_STRING)) {
      lineStyle = Grappa.DOT_LINE_SOLID;
    } else if(style.equals(Grappa.DOT_LINE_DASHED_STRING)) {
      lineStyle = Grappa.DOT_LINE_DASHED;
    } else if(style.equals(Grappa.DOT_LINE_DOTTED_STRING)) {
      lineStyle = Grappa.DOT_LINE_DOTTED;
    }
    return oldStyle;
  }

  /**
   * @return the foreground color.
   */
  public Color getForeground() {
    return foregroundColor;
  }

  /**
   * @return the background color.
   */
  public Color getBackground() {
    return backgroundColor;
  }

  /**
   * @return the x-or color.
   */
  public Color getXORColor() {
    return xorColor;
  }

  /**
   * @return the fontcolor.
   */
  public Color getFontcolor() {
    return fontcolorColor;
  }

  /**
   * @return the font name.
   */
  public String getFontname() {
    return fontname;
  }

  /**
   * @return the font size.
   */
  public int getFontsize() {
    return fontsize;
  }

  /**
   * @return the font style.
   */
  public int getFontstyle() {
    return fontstyle;
  }

  /**
   * Return the current font.  Calls setFont() before returning the font,
   * if needed.
   *
   * @return the font.
   */
  public Font getFont() {
    if(setFontFlag) {
      setFont();
    }
    return font;
  }

  /**
   * @return the x-or mode.
   */
  public boolean getXORMode() {
    return xorMode;
  }

  /**
   * @return the clipping rectangle.
   */
  public MyRectangle getClipRect() {
    return clipRect;
  }

  /**
   * @return the fill mode.
   */
  public boolean getFillMode() {
    return fillMode;
  }

  /**
   * @return the line style.
   */
  public int getLineStyle() {
    return lineStyle;
  }

  /**
   * @return the line style as a String.
   */
  public String getLineStyleString() {
    String retString = null;
    
    switch(lineStyle) {
    case Grappa.DOT_LINE_SOLID:
      retString = Grappa.DOT_LINE_SOLID_STRING;
      break;
    case Grappa.DOT_LINE_DASHED:
      retString = Grappa.DOT_LINE_DASHED_STRING;
      break;
    case Grappa.DOT_LINE_DOTTED:
      retString = Grappa.DOT_LINE_DOTTED_STRING;
      break;
    }
    return retString;
  }

  public boolean equals(GraphicContext testGC) {
    if(testGC == null) return false;
    if(!foregroundColor.equals(testGC.getForeground())) return false;
    if(!backgroundColor.equals(testGC.getBackground())) return false;
    if(!xorColor.equals(testGC.getXORColor())) return false;
    if(!fontcolorColor.equals(testGC.getFontcolor())) return false;
    if(!font.equals(testGC.getFont())) return false;
    if(xorMode != testGC.getXORMode()) return false;
    if(!clipRect.equals(testGC.getClipRect())) return false;
    return true;
  }

  public static int xlateFontStyle(String fontstyle) {
    if(fontstyle != null) {
      String style = canonColor(fontstyle);
      if(style.equals("italic")) {
	return Font.ITALIC;
      } else if(style.equals("bold")) {
	return Font.BOLD;
      }
    }
    return Font.PLAIN;
  }
}
