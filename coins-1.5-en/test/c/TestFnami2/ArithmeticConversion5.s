 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.7:
	.byte	37
	.byte	49
	.byte	50
	.byte	46
	.byte	55
	.byte	103
	.byte	32
	.byte	37
	.byte	49
	.byte	50
	.byte	46
	.byte	55
	.byte	103
	.byte	10
	.byte	0

	.align	4
	.global	_ff
_ff:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$20,%esp
	fldl	8(%ebp)
	fstpl	-8(%ebp)
	fldl	16(%ebp)
	fstpl	-16(%ebp)
	fldl	-16(%ebp)
	fstps	-20(%ebp)
	flds	-20(%ebp)
	sub	$8,%esp
	fstpl	(%esp)
	fldl	-8(%ebp)
	fstps	-20(%ebp)
	flds	-20(%ebp)
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_string.7
	call	_printf
	leal	20(%esp),%esp
.L3:
	leave
	ret

	.align	1
_string.9:
	.byte	37
	.byte	50
	.byte	50
	.byte	46
	.byte	49
	.byte	54
	.byte	103
	.byte	10
	.byte	0

	.align	4
	.global	_fd
_fd:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	fldl	8(%ebp)
	fstpl	-8(%ebp)
	pushl	4-8(%ebp)
	pushl	-8(%ebp)
	pushl	$_string.9
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
	subl	$16,%esp
	flds	_fld0
	sub	$8,%esp
	fstpl	(%esp)
	flds	_fd0
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_string.7
	call	_printf
	leal	20(%esp),%esp
	fldl	.LC1
	fstpl	-8(%ebp)
	fldl	.LC1
	fstpl	-16(%ebp)
	pushl	.LC1+4
	pushl	.LC1
	pushl	.LC1+4
	pushl	.LC1
	pushl	$_string.7
	call	_printf
	leal	20(%esp),%esp
	pushl	.LC1+4
	pushl	.LC1
	pushl	.LC1+4
	pushl	.LC1
	pushl	$_string.7
	call	_printf
	leal	20(%esp),%esp
	pushl	.LC1+4
	pushl	.LC1
	pushl	.LC1+4
	pushl	.LC1
	call	_ff
	leal	16(%esp),%esp
	flds	_fld1
	sub	$8,%esp
	fstpl	(%esp)
	flds	_fd1
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_string.7
	call	_printf
	leal	20(%esp),%esp
	fldl	.LC2
	fstpl	-8(%ebp)
	fldl	.LC2
	fstpl	-16(%ebp)
	pushl	.LC2+4
	pushl	.LC2
	pushl	.LC2+4
	pushl	.LC2
	pushl	$_string.7
	call	_printf
	leal	20(%esp),%esp
	pushl	.LC2+4
	pushl	.LC2
	pushl	.LC2+4
	pushl	.LC2
	pushl	$_string.7
	call	_printf
	leal	20(%esp),%esp
	pushl	.LC2+4
	pushl	.LC2
	pushl	.LC2+4
	pushl	.LC2
	call	_ff
	leal	16(%esp),%esp
	pushl	_dld0+4
	pushl	_dld0
	pushl	$_string.9
	call	_printf
	leal	12(%esp),%esp
	fldl	.LC3
	fstpl	-16(%ebp)
	pushl	.LC3+4
	pushl	.LC3
	pushl	$_string.9
	call	_printf
	leal	12(%esp),%esp
	pushl	.LC3+4
	pushl	.LC3
	pushl	$_string.9
	call	_printf
	leal	12(%esp),%esp
	pushl	.LC3+4
	pushl	.LC3
	call	_fd
	leal	8(%esp),%esp
	pushl	_dld1+4
	pushl	_dld1
	pushl	$_string.9
	call	_printf
	leal	12(%esp),%esp
	fldl	.LC4
	fstpl	-16(%ebp)
	pushl	.LC4+4
	pushl	.LC4
	pushl	$_string.9
	call	_printf
	leal	12(%esp),%esp
	pushl	.LC4+4
	pushl	.LC4
	pushl	$_string.9
	call	_printf
	leal	12(%esp),%esp
	pushl	.LC4+4
	pushl	.LC4
	call	_fd
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	4
	.global	_fd0
_fd0:
	.long	0x7f7fffff /* 3.4028234663852886E38 */
	.align	4
	.global	_fld0
_fld0:
	.long	0x7f7fffff /* 3.4028234663852886E38 */
	.align	4
	.global	_fd1
_fd1:
	.long	0x800000 /* 1.1754943508222875E-38 */
	.align	4
	.global	_fld1
_fld1:
	.long	0x800000 /* 1.1754943508222875E-38 */
	.align	4
	.global	_dld0
_dld0:
	.long	0xffffffff,0x7fefffff /* 1.7976931348623157E308 */
	.align	4
	.global	_dld1
_dld1:
	.long	0x0,0x100000 /* 2.2250738585072014E-308 */
	.text
	.align	4
.LC1:
	.long	0xe0000000,0x47efffff /* 3.4028234663852886E38 */
	.align	4
.LC2:
	.long	0x0,0x38100000 /* 1.1754943508222875E-38 */
	.align	4
.LC3:
	.long	0xffffffff,0x7fefffff /* 1.7976931348623157E308 */
	.align	4
.LC4:
	.long	0x0,0x100000 /* 2.2250738585072014E-308 */
