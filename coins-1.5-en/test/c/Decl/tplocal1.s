 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.7:
	.byte	105
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	106
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	103
	.byte	49
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	97
	.byte	91
	.byte	105
	.byte	93
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	97
	.byte	91
	.byte	50
	.byte	93
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$40,%esp
	movl	$1,_g1
	movl	$2,%eax
	movl	_g1,%eax
	movl	%eax,-32(%ebp)
	movl	-32(%ebp),%eax
	leal	2(%eax),%eax
	pushl	-32(%ebp)
	pushl	-32(%ebp)
	pushl	$2
	pushl	_g1
	pushl	%eax
	pushl	$2
	pushl	$_string.7
	call	_printf
	leal	28(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.comm	_g2,4
	.comm	_g3,4
	.comm	_g1,4
