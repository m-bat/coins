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

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$0,%ebx
	pushl	$0
	pushl	$_string.6
	call	_printf
	leal	8(%esp),%esp
.L3:
	cmpl	$10,%ebx
	jge	.L5
.L4:
	leal	1(%ebx),%ebx
	jmp	.L3
.L5:
	movb	$31,%al
.L6:
	cmpl	$20,%ebx
	jge	.L8
.L7:
	leal	2(%ebx),%ebx
	jmp	.L6
.L8:
	movsbl	%al,%eax
	pushl	%eax
	pushl	$_string.6
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%eax
	popl	%ebx
	leave
	ret

