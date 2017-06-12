 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.2:
	.short	97
	.short	98
	.short	99
	.short	0
	.align	1
_string.4:
	.short	99
	.short	49
	.short	61
	.short	37
	.short	99
	.short	32
	.short	115
	.short	49
	.short	61
	.short	37
	.short	99
	.short	32
	.short	115
	.short	50
	.short	61
	.short	37
	.short	100
	.short	32
	.short	99
	.short	50
	.short	61
	.short	37
	.short	100
	.short	32
	.short	32
	.short	42
	.short	112
	.short	49
	.short	61
	.short	37
	.short	100
	.short	32
	.short	42
	.short	112
	.short	50
	.short	61
	.short	37
	.short	100
	.short	10
	.short	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movw	$97,_c1
	movl	$_string.2,_s1
	movl	_s1,%eax
	leal	1(%eax),%eax
	movl	%eax,_s2
	movl	_s2,%eax
	movw	1(%eax),%ax
	movw	%ax,_c2
	movl	$1,_i
	movl	_i,%eax
	leal	_a(,%eax,4),%eax
	movl	%eax,_p1
	movl	_p1,%ecx
	movl	_i,%eax
	leal	(%ecx,%eax,4),%eax
	movl	%eax,_p2
	movl	_p2,%eax
	pushl	(%eax)
	movl	_p1,%eax
	pushl	(%eax)
	movswl	_c2,%eax
	pushl	%eax
	movl	_s2,%eax
	movswl	(%eax),%eax
	pushl	%eax
	movswl	_c1,%eax
	pushl	%eax
	movl	_s1,%eax
	movswl	(%eax),%eax
	pushl	%eax
	pushl	$_string.4
	call	_printf
	leal	28(%esp),%esp
	movl	$1,%eax
	leave
	ret

	.comm	_p1,4
	.comm	_c1,1
	.comm	_c2,1
	.comm	_x,4
	.comm	_p2,4
	.comm	_i,4
	.comm	_a,40
	.comm	_s1,4
	.comm	_s2,4
