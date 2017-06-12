 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.8:
	.byte	39
	.byte	39
	.byte	34
	.byte	63
	.byte	92
	.byte	7
	.byte	8
	.byte	12
	.byte	10
	.byte	13
	.byte	9
	.byte	11
	.byte	0
	.align	1
_string.9:
	.byte	1
	.byte	10
	.byte	83
	.byte	83
	.byte	52
	.byte	46
	.byte	55
	.byte	7
	.byte	56
	.byte	0
	.align	1
_string.10:
	.byte	1
	.byte	18
	.byte	18
	.byte	-85
	.byte	12
	.byte	68
	.byte	15
	.byte	71
	.byte	0
	.align	1
_string.12:
	.byte	97
	.byte	58
	.byte	0
	.align	1
_string.14:
	.byte	32
	.byte	37
	.byte	48
	.byte	50
	.byte	88
	.byte	0
	.align	1
_string.16:
	.byte	10
	.byte	98
	.byte	58
	.byte	0
	.align	1
_string.19:
	.byte	10
	.byte	99
	.byte	58
	.byte	0
	.align	1
_string.22:
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$_string.8,%edi
	movl	$_string.9,%esi
	movl	$_string.10,%ebx
	pushl	$_string.12
	call	_printf
	leal	4(%esp),%esp
.L3:
	movb	(%edi),%al
	cmpb	$0,%al
	je	.L5
.L4:
	movsbl	(%edi),%eax
	andl	$255,%eax
	leal	1(%edi),%edi
	pushl	%eax
	pushl	$_string.14
	call	_printf
	leal	8(%esp),%esp
	jmp	.L3
.L5:
	pushl	$_string.16
	call	_printf
	leal	4(%esp),%esp
.L6:
	movb	(%esi),%al
	cmpb	$0,%al
	je	.L8
.L7:
	movsbl	(%esi),%eax
	andl	$255,%eax
	leal	1(%esi),%esi
	pushl	%eax
	pushl	$_string.14
	call	_printf
	leal	8(%esp),%esp
	jmp	.L6
.L8:
	pushl	$_string.19
	call	_printf
	leal	4(%esp),%esp
.L9:
	movb	(%ebx),%al
	cmpb	$0,%al
	je	.L11
.L10:
	movsbl	(%ebx),%eax
	andl	$255,%eax
	leal	1(%ebx),%ebx
	pushl	%eax
	pushl	$_string.14
	call	_printf
	leal	8(%esp),%esp
	jmp	.L9
.L11:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%eax
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret

