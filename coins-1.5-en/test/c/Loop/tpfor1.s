 .ident "Coins Compiler version: coins-1.4.4.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.3:
	.byte	97
	.byte	91
	.byte	37
	.byte	100
	.byte	93
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$0,_i
.L3:
	movl	_i,%eax
	cmpl	$10,%eax
	jge	.L5
.L4:
	movl	_i,%eax
	movl	$0,_a(,%eax,4)
	movl	_i,%eax
	leal	1(%eax),%eax
	movl	%eax,_i
	jmp	.L3
.L5:
	movl	$1,_i
.L6:
	movl	_i,%eax
	cmpl	$10,%eax
	jge	.L8
.L7:
	movl	_i,%eax
	leal	-1(%eax),%eax
	movl	_a(,%eax,4),%eax
	addl	_i,%eax
	movl	_i,%ecx
	movl	%eax,_a(,%ecx,4)
	movl	_i,%eax
	leal	1(%eax),%eax
	movl	%eax,_i
	jmp	.L6
.L8:
	movl	$0,_i
.L9:
	movl	_i,%eax
	cmpl	$10,%eax
	jge	.L11
.L10:
	movl	_i,%eax
	pushl	_a(,%eax,4)
	pushl	_i
	pushl	$_string.3
	call	_printf
	leal	12(%esp),%esp
	movl	_i,%eax
	leal	1(%eax),%eax
	movl	%eax,_i
	jmp	.L9
.L11:
	movl	$0,%eax
	leave
	ret

	.comm	_i,4
	.comm	_a,40
