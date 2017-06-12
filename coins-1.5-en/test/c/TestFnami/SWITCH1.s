 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.5:
	.byte	111
	.byte	110
	.byte	101
	.byte	10
	.byte	0
	.align	1
_string.7:
	.byte	116
	.byte	119
	.byte	111
	.byte	10
	.byte	0
	.align	1
_string.9:
	.byte	109
	.byte	97
	.byte	110
	.byte	121
	.byte	10
	.byte	0
	.align	1
_string.11:
	.byte	117
	.byte	110
	.byte	107
	.byte	110
	.byte	111
	.byte	119
	.byte	110
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	cmpl	$1,%ecx
	je	.L3
.L9:
	cmpl	$2,%ecx
	je	.L4
.L10:
	cmpl	$3,%ecx
	je	.L5
.L12:
	jmp	.L6
.L3:
	pushl	$_string.5
	call	_printf
	leal	4(%esp),%esp
	jmp	.L7
.L4:
	pushl	$_string.7
	call	_printf
	leal	4(%esp),%esp
	jmp	.L7
.L5:
	pushl	$_string.9
	call	_printf
	leal	4(%esp),%esp
	jmp	.L7
.L6:
	pushl	$_string.11
	call	_printf
	leal	4(%esp),%esp
.L7:
	movl	$0,%eax
	leave
	ret

