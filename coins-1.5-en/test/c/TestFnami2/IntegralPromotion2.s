 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.12:
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	10
	.byte	0

	.align	4
	.global	_f
_f:
	pushl	%ebp
	movl	%esp,%ebp
	movb	8(%ebp),%ch
	movb	12(%ebp),%cl
	movb	16(%ebp),%al
	movzbl	%al,%eax
	negl	%eax
	pushl	%eax
	movsbl	%cl,%eax
	negl	%eax
	pushl	%eax
	movsbl	%ch,%eax
	negl	%eax
	pushl	%eax
	pushl	$_string.12
	call	_printf
	leal	16(%esp),%esp
.L3:
	leave
	ret

	.align	1
_string.14:
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	32
	.byte	37
	.byte	48
	.byte	56
	.byte	88
	.byte	10
	.byte	0

	.align	4
	.global	_g
_g:
	pushl	%ebp
	movl	%esp,%ebp
	movw	8(%ebp),%cx
	movw	12(%ebp),%ax
	movzwl	%ax,%eax
	negl	%eax
	pushl	%eax
	movswl	%cx,%eax
	negl	%eax
	pushl	%eax
	pushl	$_string.14
	call	_printf
	leal	12(%esp),%esp
.L6:
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	_i2
	pushl	_i1
	pushl	_i0
	pushl	$_string.12
	call	_printf
	leal	16(%esp),%esp
	movb	$2,%al
	movb	$2,%al
	movb	$2,%al
	pushl	$-2
	pushl	$-2
	pushl	$-2
	pushl	$_string.12
	call	_printf
	leal	16(%esp),%esp
	pushl	$-2
	pushl	$-2
	pushl	$-2
	pushl	$_string.12
	call	_printf
	leal	16(%esp),%esp
	pushl	$3
	pushl	$3
	pushl	$3
	call	_f
	leal	12(%esp),%esp
	pushl	_i4
	pushl	_i3
	pushl	$_string.14
	call	_printf
	leal	12(%esp),%esp
	movw	$2,%ax
	movw	$2,%ax
	pushl	$-2
	pushl	$-2
	pushl	$_string.14
	call	_printf
	leal	12(%esp),%esp
	pushl	$-2
	pushl	$-2
	pushl	$_string.14
	call	_printf
	leal	12(%esp),%esp
	pushl	$3
	pushl	$3
	call	_g
	leal	8(%esp),%esp
	pushl	_i7
	pushl	_i6
	pushl	_i5
	pushl	$_string.12
	call	_printf
	leal	16(%esp),%esp
	movb	$-2,%al
	movb	$-2,%al
	movb	$-2,%al
	pushl	$-254
	pushl	$2
	pushl	$2
	pushl	$_string.12
	call	_printf
	leal	16(%esp),%esp
	pushl	$-254
	pushl	$2
	pushl	$2
	pushl	$_string.12
	call	_printf
	leal	16(%esp),%esp
	pushl	$253
	pushl	$253
	pushl	$253
	call	_f
	leal	12(%esp),%esp
	pushl	_i9
	pushl	_i8
	pushl	$_string.14
	call	_printf
	leal	12(%esp),%esp
	movw	$-2,%ax
	movw	$-2,%ax
	pushl	$-65534
	pushl	$2
	pushl	$_string.14
	call	_printf
	leal	12(%esp),%esp
	pushl	$-65534
	pushl	$2
	pushl	$_string.14
	call	_printf
	leal	12(%esp),%esp
	pushl	$65533
	pushl	$65533
	call	_g
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	4
	.global	_i0
_i0:
	.long	-1
	.align	4
	.global	_i1
_i1:
	.long	-1
	.align	4
	.global	_i2
_i2:
	.long	-1
	.align	4
	.global	_i3
_i3:
	.long	-1
	.align	4
	.global	_i4
_i4:
	.long	-1
	.align	4
	.global	_i5
_i5:
	.long	1
	.align	4
	.global	_i6
_i6:
	.long	1
	.align	4
	.global	_i7
_i7:
	.long	-255
	.align	4
	.global	_i8
_i8:
	.long	1
	.align	4
	.global	_i9
_i9:
	.long	-65535
