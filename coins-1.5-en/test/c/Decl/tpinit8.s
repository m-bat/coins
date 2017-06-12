 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.6:
	.byte	97
	.byte	91
	.byte	48
	.byte	93
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	98
	.byte	91
	.byte	48
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
	subl	$20,%esp
	movl	$1,-8(%ebp)
	movl	$0,-4(%ebp)
	movl	$1,-20(%ebp)
	movl	$1,%eax
.L3:
	cmpl	$3,%eax
	jge	.L5
.L4:
	movl	$0,-20(%ebp,%eax,4)
	leal	1(%eax),%eax
	jmp	.L3
.L5:
	pushl	-20(%ebp)
	pushl	-8(%ebp)
	pushl	$_string.6
	call	_printf
	leal	12(%esp),%esp
	movl	$0,%eax
	leave
	ret

