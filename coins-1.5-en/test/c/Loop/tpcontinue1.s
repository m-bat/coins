 .ident "Coins Compiler version: coins-1.4.4.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.3:
	.byte	97
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	98
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	99
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	105
	.byte	61
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$1,_i
	movl	$0,_a
	movl	$1,_b
.L4:
	movl	_i,%eax
	cmpl	$10,%eax
	jge	.L9
.L5:
	movl	_a,%eax
	addl	_b,%eax
	movl	%eax,_a
	movl	_a,%eax
	cmpl	$10,%eax
	jl	.L7
.L6:
	movl	_i,%eax
	leal	1(%eax),%eax
	movl	%eax,_i
	jmp	.L4
.L7:
	movl	_i,%eax
	leal	1(%eax),%eax
	movl	%eax,_i
	jmp	.L4
.L9:
	movl	_a,%eax
	addl	_b,%eax
	movl	%eax,_c
	pushl	_i
	pushl	_c
	pushl	_b
	pushl	_a
	pushl	$_string.3
	call	_printf
	leal	20(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.comm	_a,4
	.comm	_c,4
	.comm	_i,4
	.comm	_b,4
