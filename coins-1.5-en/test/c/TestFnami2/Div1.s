 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	movl	$214748364,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$171,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$86,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$6,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$5,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$4,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$3,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$2,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	pushl	%ebx
	pushl	$_s9
	call	_printf
	leal	40(%esp),%esp
	movl	$-214748364,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-171,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-86,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-6,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-5,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-4,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-3,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-2,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-1,%ecx
	movl	%ebx,%eax
	cdq
	idivl	%ecx
	pushl	%eax
	pushl	$_s9
	call	_printf
	leal	40(%esp),%esp
.L3:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f1
_f1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	movl	$214748364,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$171,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$86,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$6,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$5,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$4,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$3,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$2,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$1,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$0,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	pushl	$_s10
	call	_printf
	leal	44(%esp),%esp
	movl	$-214748364,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-171,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-86,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-6,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-5,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-4,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-3,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-2,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	movl	$-1,%eax
	movl	%ebx,%ecx
	cdq
	idivl	%ecx
	pushl	%eax
	pushl	$_s9
	call	_printf
	leal	40(%esp),%esp
.L6:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_op
_op:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%ecx
	cdq
	idivl	%ecx
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$44,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%eax
	movl	%eax,-12(%ebp)
	pushl	$1
	pushl	-12(%ebp)
	call	_op
	movl	%eax,-44(%ebp)
	leal	8(%esp),%esp
	pushl	$2
	pushl	-12(%ebp)
	call	_op
	movl	%eax,-36(%ebp)
	leal	8(%esp),%esp
	pushl	$3
	pushl	-12(%ebp)
	call	_op
	movl	%eax,-28(%ebp)
	leal	8(%esp),%esp
	pushl	$4
	pushl	-12(%ebp)
	call	_op
	movl	%eax,-20(%ebp)
	leal	8(%esp),%esp
	pushl	$5
	pushl	-12(%ebp)
	call	_op
	movl	%eax,-8(%ebp)
	leal	8(%esp),%esp
	pushl	$6
	pushl	-12(%ebp)
	call	_op
	movl	%eax,%edi
	leal	8(%esp),%esp
	pushl	$86
	pushl	-12(%ebp)
	call	_op
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	$171
	pushl	-12(%ebp)
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$214748364
	pushl	-12(%ebp)
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-8(%ebp)
	pushl	-20(%ebp)
	pushl	-28(%ebp)
	pushl	-36(%ebp)
	pushl	-44(%ebp)
	pushl	$_s9
	call	_printf
	leal	40(%esp),%esp
	pushl	$-1
	pushl	-12(%ebp)
	call	_op
	movl	%eax,-40(%ebp)
	leal	8(%esp),%esp
	pushl	$-2
	pushl	-12(%ebp)
	call	_op
	movl	%eax,-32(%ebp)
	leal	8(%esp),%esp
	pushl	$-3
	pushl	-12(%ebp)
	call	_op
	movl	%eax,-24(%ebp)
	leal	8(%esp),%esp
	pushl	$-4
	pushl	-12(%ebp)
	call	_op
	movl	%eax,-16(%ebp)
	leal	8(%esp),%esp
	pushl	$-5
	pushl	-12(%ebp)
	call	_op
	movl	%eax,-4(%ebp)
	leal	8(%esp),%esp
	pushl	$-6
	pushl	-12(%ebp)
	call	_op
	movl	%eax,%edi
	leal	8(%esp),%esp
	pushl	$-86
	pushl	-12(%ebp)
	call	_op
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	$-171
	pushl	-12(%ebp)
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$-214748364
	pushl	-12(%ebp)
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-4(%ebp)
	pushl	-16(%ebp)
	pushl	-24(%ebp)
	pushl	-32(%ebp)
	pushl	-40(%ebp)
	pushl	$_s9
	call	_printf
	leal	40(%esp),%esp
.L12:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	_a9
	pushl	_a8
	pushl	_a7
	pushl	_a6
	pushl	_a5
	pushl	_a4
	pushl	_a3
	pushl	_a2
	pushl	_a1
	pushl	$_s9
	call	_printf
	leal	40(%esp),%esp
	pushl	_b9
	pushl	_b8
	pushl	_b7
	pushl	_b6
	pushl	_b5
	pushl	_b4
	pushl	_b3
	pushl	_b2
	pushl	_b1
	pushl	$_s9
	call	_printf
	leal	40(%esp),%esp
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$1
	pushl	$1
	pushl	$2
	pushl	$3
	pushl	$4
	pushl	$9
	pushl	$_s9
	call	_printf
	leal	40(%esp),%esp
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$-1
	pushl	$-1
	pushl	$-2
	pushl	$-3
	pushl	$-4
	pushl	$-9
	pushl	$_s9
	call	_printf
	leal	40(%esp),%esp
	movl	$9,%eax
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$1
	pushl	$1
	pushl	$2
	pushl	$3
	pushl	$4
	pushl	$9
	pushl	$_s9
	call	_printf
	leal	40(%esp),%esp
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$1
	pushl	$1
	pushl	$2
	pushl	$3
	pushl	$4
	pushl	$9
	pushl	$_s9
	call	_printf
	leal	40(%esp),%esp
	movl	$9,%eax
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$-1
	pushl	$-1
	pushl	$-2
	pushl	$-3
	pushl	$-4
	pushl	$-9
	pushl	$_s9
	call	_printf
	leal	40(%esp),%esp
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$-1
	pushl	$-1
	pushl	$-2
	pushl	$-3
	pushl	$-4
	pushl	$-9
	pushl	$_s9
	call	_printf
	leal	40(%esp),%esp
	movl	$9,%eax
	pushl	$23860929
	pushl	$19
	pushl	$9
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$_s10
	call	_printf
	leal	44(%esp),%esp
	pushl	$23860929
	pushl	$19
	pushl	$9
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$_s10
	call	_printf
	leal	44(%esp),%esp
	movl	$9,%eax
	pushl	$-23860929
	pushl	$-19
	pushl	$-9
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$_s9
	call	_printf
	leal	40(%esp),%esp
	pushl	$-23860929
	pushl	$-19
	pushl	$-9
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$0
	pushl	$_s9
	call	_printf
	leal	40(%esp),%esp
	pushl	$9
	call	_f0
	leal	4(%esp),%esp
	pushl	$-9
	call	_f0
	leal	4(%esp),%esp
	pushl	$2147483647
	call	_f0
	leal	4(%esp),%esp
	pushl	$-2147483647
	call	_f0
	leal	4(%esp),%esp
	pushl	$9
	call	_f0
	leal	4(%esp),%esp
	pushl	$-9
	call	_f0
	leal	4(%esp),%esp
	pushl	$2147483647
	call	_f1
	leal	4(%esp),%esp
	pushl	$-2147483647
	call	_f1
	leal	4(%esp),%esp
	pushl	$9
	call	_f0
	leal	4(%esp),%esp
	pushl	$-9
	call	_f0
	leal	4(%esp),%esp
	pushl	$2147483647
	call	_f2
	leal	4(%esp),%esp
	pushl	$-2147483647
	call	_f2
	leal	4(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	1
	.global	_s9
_s9:
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	1
	.global	_s10
_s10:
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	4
	.global	_a1
_a1:
	.long	9
	.align	4
	.global	_a2
_a2:
	.long	4
	.align	4
	.global	_a3
_a3:
	.long	3
	.align	4
	.global	_a4
_a4:
	.long	2
	.align	4
	.global	_a5
_a5:
	.long	1
	.align	4
	.global	_a6
_a6:
	.long	1
	.align	4
	.global	_a7
_a7:
	.long	0
	.align	4
	.global	_a8
_a8:
	.long	0
	.align	4
	.global	_a9
_a9:
	.long	0
	.align	4
	.global	_b1
_b1:
	.long	-9
	.align	4
	.global	_b2
_b2:
	.long	-4
	.align	4
	.global	_b3
_b3:
	.long	-3
	.align	4
	.global	_b4
_b4:
	.long	-2
	.align	4
	.global	_b5
_b5:
	.long	-1
	.align	4
	.global	_b6
_b6:
	.long	-1
	.align	4
	.global	_b7
_b7:
	.long	0
	.align	4
	.global	_b8
_b8:
	.long	0
	.align	4
	.global	_b9
_b9:
	.long	0
