 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.4:
	.byte	65
	.byte	66
	.byte	67
	.byte	68
	.byte	0
	.align	1
_string.6:
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	1
_string.8:
	.byte	37
	.byte	99
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	$_string.4,%ebx
	movl	$_a,%esi
	pushl	$6
	pushl	$_string.6
	call	_printf
	leal	8(%esp),%esp
	movswl	_a+2,%eax
	pushl	%eax
	movsbl	_string.4+1,%eax
	pushl	%eax
	pushl	$_string.8
	call	_printf
	leal	12(%esp),%esp
	movswl	4(%esi),%eax
	pushl	%eax
	movsbl	2(%ebx),%eax
	pushl	%eax
	pushl	$_string.8
	call	_printf
	leal	12(%esp),%esp
	movl	$0,%eax
	popl	%esi
	popl	%ebx
	leave
	ret

	.data
	.align	2
	.global	_a
_a:
	.short	123
	.short	456
	.short	789
