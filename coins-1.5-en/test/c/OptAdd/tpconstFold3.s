 .ident "Coins Compiler version: coins-1.4.4.3 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.4:
	.byte	121
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	107
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	106
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
	pushl	$1
	call	_f
	movl	%eax,_x
	leal	4(%esp),%esp
	movl	_x,%eax
	leal	110(%eax),%eax
	movl	%eax,_i
	pushl	$2
	call	_f
	movl	%eax,_y
	leal	4(%esp),%esp
	movl	_y,%eax
	leal	10(%eax),%eax
	movl	%eax,_y
	movl	$6,%eax
	imull	_j,%eax
	addl	_x,%eax
	movl	%eax,_k
	movl	$3,_a
	movl	$4,_a+4
	movl	$-1,_i
	movl	_i,%eax
	negl	%eax
	addl	_a+4,%eax
	movl	%eax,_j
	pushl	_j
	pushl	_k
	pushl	_y
	pushl	$_string.4
	call	_printf
	leal	16(%esp),%esp
	movl	$0,%eax
	leave
	ret


	.align	4
	.global	_f
_f:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	leave
	ret

	.comm	_i,4
	.comm	_a,40
	.comm	_x,4
	.comm	_y,4
	.comm	_j,4
	.comm	_k,4
