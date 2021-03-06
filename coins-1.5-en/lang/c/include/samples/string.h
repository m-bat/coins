extern void *memcpy (void *s, void *ct, int n);
extern void *memmove (void *s, void *ct, int n);
extern void *memset (void *s, int c, int n);
extern int memcmp (void *cs, void *ct, int n);
extern void *memchr (void *cs, int c, int n);
extern char *strcpy (char *s, char *ct);
extern char *strncpy (char *s, char *ct, int n);
extern char *strcat (char *s, char *ct);
extern char *strncat (char *s, char *ct, int n);
extern int strcmp (char *cs, char *ct);
extern int strncmp (char *cs, char *ct, int n);
extern char *strchr (char *s, int c);
extern char *strrchr (char *s, int c);
extern size_t strspn (char *cs, char *ct);
extern size_t strcspn (char *cs, char *ct);
extern char *strpbrk (char *cs, char *ct);
extern char *strstr (char *cs, char *ct);
extern size_t strlen (char *cs);
extern char *strerror (int n);
extern char *strtok (char *s, char *ct);
