 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.7:
	.byte	37
	.byte	108
	.byte	100
	.byte	32
	.byte	37
	.byte	108
	.byte	120
	.byte	32
	.byte	37
	.byte	108
	.byte	108
	.byte	100
	.byte	32
	.byte	37
	.byte	108
	.byte	108
	.byte	120
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$-536870912,%eax
	movl	$3758096384,%eax
	movl	$0,%edx
	movl	$-536870912,%eax
	movl	$3758096384,%eax
	movl	$0,%edx
	pushl	$0
	pushl	$3758096384
	pushl	$0
	pushl	$3758096384
	pushl	$-536870912
	pushl	$-536870912
	pushl	$_string.7
	call	_printf
	leal	28(%esp),%esp
	pushl	$0
	pushl	$3758096384
	pushl	$0
	pushl	$3758096384
	pushl	$-536870912
	pushl	$-536870912
	pushl	$_string.7
	call	_printf
	leal	28(%esp),%esp
	movl	$0,%eax
	leave
	ret

