/* tpcpp1.c -- C preprocessor */
#include <stdio.h>
int main(int argc, char** argv) {
  printf("%s\n",
#if defined(_GNU_SOURCE)
   "Yes, I use _GNU_SOURCE !"
#elif defined(__USE_GNU)
   "Yes, I use __USE_GNU !"
#else
   "No, I don't use GNU extension"
#endif
        );
  return 0;
}

