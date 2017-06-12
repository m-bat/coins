 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.7:
	.byte	97
	.byte	58
	.byte	0
	.align	1
_string.9:
	.byte	32
	.byte	37
	.byte	48
	.byte	50
	.byte	88
	.byte	0
	.align	1
_string.11:
	.byte	10
	.byte	98
	.byte	58
	.byte	0
	.align	1
_string.14:
	.byte	10
	.byte	99
	.byte	58
	.byte	0
	.align	1
_string.17:
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$20,%esp
	pushl	%ebx
	movb	$34,-13(%ebp)
	movb	$63,-12(%ebp)
	movb	$39,-11(%ebp)
	movb	$34,-10(%ebp)
	movb	$63,-9(%ebp)
	movb	$92,-8(%ebp)
	movb	$7,-7(%ebp)
	movb	$8,-6(%ebp)
	movb	$12,-5(%ebp)
	movb	$10,-4(%ebp)
	movb	$13,-3(%ebp)
	movb	$9,-2(%ebp)
	movb	$11,-1(%ebp)
	movb	$1,-16(%ebp)
	movb	$10,-15(%ebp)
	movb	$83,-14(%ebp)
	movb	$1,-20(%ebp)
	movb	$18,-19(%ebp)
	movb	$18,-18(%ebp)
	movb	$-85,-17(%ebp)
	pushl	$_string.7
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%ebx
.L3:
	cmpl	$13,%ebx
	jge	.L5
.L4:
	movsbl	-13(%ebp,%ebx),%eax
	andl	$255,%eax
	pushl	%eax
	pushl	$_string.9
	call	_printf
	leal	8(%esp),%esp
	leal	1(%ebx),%ebx
	jmp	.L3
.L5:
	pushl	$_string.11
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%ebx
.L6:
	cmpl	$3,%ebx
	jge	.L8
.L7:
	movsbl	-16(%ebp,%ebx),%eax
	andl	$255,%eax
	pushl	%eax
	pushl	$_string.9
	call	_printf
	leal	8(%esp),%esp
	leal	1(%ebx),%ebx
	jmp	.L6
.L8:
	pushl	$_string.14
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%ebx
.L9:
	cmpl	$4,%ebx
	jge	.L11
.L10:
	movsbl	-20(%ebp,%ebx),%eax
	andl	$255,%eax
	pushl	%eax
	pushl	$_string.9
	call	_printf
	leal	8(%esp),%esp
	leal	1(%ebx),%ebx
	jmp	.L9
.L11:
	pushl	$_string.17
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%eax
	popl	%ebx
	leave
	ret

