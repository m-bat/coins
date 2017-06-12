 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.4:
	.byte	37
	.byte	100
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
	movl	$-48,%eax
	sarl	%cl,%eax
	pushl	%eax
	movl	$48,%eax
	sarl	%cl,%eax
	pushl	%eax
	pushl	$_string.4
	call	_printf
	leal	12(%esp),%esp
.L3:
	leave
	ret

	.align	1
_string.7:
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
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
	pushl	$4
	pushl	$-48
	pushl	$48
	pushl	$_string.7
	call	_printf
	leal	16(%esp),%esp
	pushl	$-12
	pushl	$12
	pushl	$_string.4
	call	_printf
	leal	12(%esp),%esp
	movl	$3,%eax
	pushl	$-6
	pushl	$6
	pushl	$_string.4
	call	_printf
	leal	12(%esp),%esp
	pushl	$-6
	pushl	$6
	pushl	$_string.4
	call	_printf
	leal	12(%esp),%esp
	pushl	$4
	call	_f
	leal	4(%esp),%esp
	movl	$0,%eax
	leave
	ret

