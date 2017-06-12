 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.4:
	.byte	37
	.byte	117
	.byte	10
	.byte	0
	.align	1
_string.6:
	.byte	50
	.byte	94
	.byte	51
	.byte	49
	.byte	45
	.byte	49
	.byte	10
	.byte	0
	.align	1
_string.8:
	.byte	50
	.byte	94
	.byte	51
	.byte	49
	.byte	10
	.byte	0
	.align	1
_string.10:
	.byte	50
	.byte	94
	.byte	51
	.byte	49
	.byte	43
	.byte	49
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	_a,%ebx
.L3:
	cmpl	_b,%ebx
	je	.L9
.L4:
	pushl	%ebx
	pushl	$_string.4
	call	_printf
	leal	8(%esp),%esp
	cmpl	$2147483647,%ebx
	je	.L5
.L12:
	cmpl	$-2147483648,%ebx
	je	.L6
.L13:
	cmpl	$-2147483647,%ebx
	je	.L7
.L15:
	jmp	.L8
.L5:
	pushl	$_string.6
	call	_printf
	leal	4(%esp),%esp
	jmp	.L8
.L6:
	pushl	$_string.8
	call	_printf
	leal	4(%esp),%esp
	jmp	.L8
.L7:
	pushl	$_string.10
	call	_printf
	leal	4(%esp),%esp
.L8:
	leal	1(%ebx),%ebx
	jmp	.L3
.L9:
	movl	$0,%eax
	popl	%ebx
	leave
	ret

	.data
	.align	4
	.global	_a
_a:
	.long	2147483640
	.align	4
	.global	_b
_b:
	.long	-2147483640
