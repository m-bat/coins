/* ---------------------------------------------------------------------
%   Copyright (C) 2007 Association for the COINS Compiler Infrastructure
%       (Read COPYING for detailed information.)
--------------------------------------------------------------------- */
/*  Original author: Shigeru Chiba in 2000-2002.                      */
package coins.cfront;

final class KeywordTable {
    char[][] names;
    int[] tokenIds;
    int num;

    KeywordTable(int n) {
  num = 0;
  names = new char[n][];
  tokenIds = new int[n];
    }

    void append(String name, int id) {
  int len = name.length();
  if (len > MAX)
      throw new RuntimeException("too long keyword");

  char[] c = new char[len];
  name.getChars(0, len, c, 0);
  names[num] = c;
  tokenIds[num] = id;
  ++num;
    }

    private static final int MAX = 32;
    //private static final char[] cbuf = new char[MAX]; //SF050304
    private final char[] cbuf = new char[MAX]; //SF050304

    int lookup(StringBuffer key) {
  int len = key.length();
  if (len > MAX)
      return -1;

  key.getChars(0, len, cbuf, 0);
  int low = 0;
  int high = num - 1;
  while (low <= high) {
      int mid = low + (high - low) / 2;
      char[] candidate = names[mid];
      int cond = compare(candidate, candidate.length, cbuf, len);
      if (cond == 0)
    return tokenIds[mid];
      else if (cond < 0)
    low = mid + 1;
      else
    high = mid - 1;
  }

  return -1;
    }

    private static int compare(char[] str1, int len1, char[] str2, int len2) {
  int n = Math.min(len1, len2);
  for (int i = 0; i < n; ++i) {
      char c1 = str1[i];
      char c2 = str2[i];
      if (c1 < c2)
    return -1;
      else if (c1 > c2)
    return 1;
  }

  if (len1 < len2)
      return -1;
  else if (len1 > len2)
      return 1;
  else
      return 0;
    }
}

