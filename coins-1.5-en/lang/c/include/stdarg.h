#ifndef _COINS_STDARG_H
#define _COINS_STDARG_H

extern void *__builtin_va_start();
extern void *__builtin_align(void *, int);

/* typedef void *va_list;*/
#define va_list void *

#undef va_start
#undef va_arg
#undef va_end

#define va_start(p, arg) ((p) = __builtin_va_start(arg))

#ifdef __sparc__
#define va_arg(p, type) ((p) = (char *)(p) + sizeof(type), \
        (*(type *)__builtin_align((char *)(p) - sizeof(type), 4)))
#endif

#ifdef __x86__
#define va_arg(p, type) ((p) = (char *)(p) + sizeof(type), \
        (*(type *)((char *)(p) - (sizeof(type)))))
#endif

#ifdef __arm__
#define va_arg(p, type) ((p) = (char *)(p) + sizeof(type), \
        (*(type *)__builtin_align((char *)(p) - sizeof(type), 4)))
#endif

#ifdef __x86_64__
#undef va_list
#undef va_start

typedef struct __va_list_element {
       unsigned int __va_gp_offset;
       unsigned int __va_fp_offset;
       void *__va_overflow_arg_area;
       void *__va_reg_sve_area;
} va_list[1];

extern long __va_start(void *p, ...);
extern long __va_arg(void *p, ...);
extern long __dummy;

#define va_start(p, arg)  __va_start(p, arg)
#define va_arg(p, type) *(type *)__va_arg(p, (type)__dummy)
#endif

#define va_end(p) ((void)0)


/* glibc requires __gnuc_va_list. */
#ifdef __linux__
#ifndef __GNUC_VA_LIST
#define __GNUC_VA_LIST
#if defined(__svr4__) || defined(_AIX) || defined(_M_UNIX) || defined(__NetBSD__)
typedef char *__gnuc_va_list;
#else
typedef void *__gnuc_va_list;
#endif
#endif
#endif

#ifdef __arm__
/* glibc requires _G_va_list. */
# ifndef _VA_LIST_DEFINED
typedef va_list _G_va_list;
# endif
#endif

#endif
