 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.6:
	.byte	37
	.byte	99
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_f
_f:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	movswl	4(%eax),%eax
	pushl	%eax
	movsbl	2(%ecx),%eax
	pushl	%eax
	pushl	$_string.6
	call	_printf
	leal	12(%esp),%esp
.L3:
	leave
	ret

	.align	1
_string.9:
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	1
_string.10:
	.byte	65
	.byte	66
	.byte	67
	.byte	68
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	$2
	pushl	$_string.9
	call	_printf
	leal	8(%esp),%esp
	movl	$_string.10+1,%ecx
	movl	$_a+2,%eax
	movswl	(%eax),%eax
	pushl	%eax
	movsbl	(%ecx),%eax
	pushl	%eax
	pushl	$_string.6
	call	_printf
	leal	12(%esp),%esp
	pushl	$_a
	pushl	$_string.10
	call	_f
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	2
	.global	_a
_a:
	.short	123
	.short	456
	.short	789
