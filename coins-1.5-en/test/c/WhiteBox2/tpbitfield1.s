 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.5:
	.byte	117
	.byte	46
	.byte	116
	.byte	46
	.byte	99
	.byte	32
	.byte	61
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	1
_string.7:
	.byte	117
	.byte	46
	.byte	116
	.byte	46
	.byte	100
	.byte	32
	.byte	61
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	1
_string.9:
	.byte	50
	.byte	53
	.byte	49
	.byte	54
	.byte	53
	.byte	56
	.byte	50
	.byte	52
	.byte	61
	.byte	37
	.byte	120
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	movb	$0,-4(%ebp)
	movb	$0,-3(%ebp)
	leal	-4(%ebp),%ecx
	movl	(%ecx),%eax
	andl	$-385,%eax
	orl	$384,%eax
	movl	%eax,(%ecx)
	movzbl	-4(%ebp),%eax
	pushl	%eax
	pushl	$_string.5
	call	_printf
	leal	8(%esp),%esp
	movzbl	-3(%ebp),%eax
	pushl	%eax
	pushl	$_string.7
	call	_printf
	leal	8(%esp),%esp
	pushl	$25165824
	pushl	$_string.9
	call	_printf
	leal	8(%esp),%esp
.L3:
	leave
	ret

