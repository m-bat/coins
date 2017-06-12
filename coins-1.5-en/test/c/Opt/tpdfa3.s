 .ident "Coins Compiler version: coins-1.4.4 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
___sgetc:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%esi
	leal	4(%esi),%edi
	movl	(%edi),%eax
	leal	-1(%eax),%eax
	movl	%eax,(%edi)
	cmpl	$0,%eax
	jge	.L4
.L3:
	pushl	%esi
	call	___srget
	movl	%eax,%ebx
	leal	4(%esp),%esp
	jmp	.L5
.L4:
	movl	(%esi),%eax
	movzbl	(%eax),%ebx
	leal	1(%eax),%eax
	movl	%eax,(%esi)
.L5:
	movswl	12(%esi),%eax
	andl	$16384,%eax
	cmpl	$0,%eax
	je	.L19
.L6:
	cmpl	$13,%ebx
	jne	.L8
.L7:
	movl	$1,%eax
	jmp	.L9
.L8:
	movl	$0,%eax
.L9:
	movw	%ax,%ax
	movswl	%ax,%eax
	cmpl	$0,%eax
	je	.L19
.L10:
	movl	(%edi),%eax
	leal	-1(%eax),%eax
	movl	%eax,(%edi)
	cmpl	$0,%eax
	jge	.L12
.L11:
	pushl	%esi
	call	___srget
	leal	4(%esp),%esp
	jmp	.L13
.L12:
	movl	(%esi),%ecx
	movzbl	(%ecx),%eax
	leal	1(%ecx),%ecx
	movl	%ecx,(%esi)
.L13:
	cmpl	$10,%eax
	jne	.L15
.L14:
	movl	%eax,%ebx
	jmp	.L19
.L15:
	pushl	%esi
	pushl	%eax
	call	_ungetc
	leal	8(%esp),%esp
.L19:
	movl	%ebx,%eax
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret

	.align	1
_string.9:
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
	.byte	100
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
	movl	$5,_m
	movl	$1,_b
	movl	_m,%eax
	leal	1(%eax),%eax
	movl	%eax,_c
	movl	_j,%eax
	leal	1(%eax),%eax
	movl	%eax,_k
	movl	_b,%eax
	addl	_c,%eax
	movl	%eax,_a
	pushl	_d
	pushl	_c
	pushl	_b
	pushl	%eax
	pushl	$_string.9
	call	_printf
	leal	20(%esp),%esp
	movl	_i,%eax
	cmpl	$10,%eax
	jge	.L27
.L23:
	movl	_j,%eax
	cmpl	$5,%eax
	jle	.L25
.L24:
	movl	$6,_n
	movl	_j,%eax
	leal	1(%eax),%eax
	movl	%eax,_k
	jmp	.L26
.L25:
	leal	1(%eax),%eax
	movl	%eax,_j
	movl	_m,%eax
	subl	_n,%eax
	movl	%eax,_m
	movl	_b,%eax
	addl	_c,%eax
	movl	%eax,_d
.L26:
	movl	_i,%eax
	leal	1(%eax),%eax
	movl	%eax,_i
	cmpl	$10,%eax
	jl	.L23
.L27:
	movl	_b,%eax
	addl	_c,%eax
	movl	%eax,_a
	addl	_d,%eax
	movl	%eax,_d
	pushl	%eax
	pushl	_c
	pushl	_b
	pushl	_a
	pushl	$_string.9
	call	_printf
	leal	20(%esp),%esp
	movl	_d,%eax
	leave
	ret

	.data
	.align	4
	.global	_i
_i:
	.long	0
	.align	4
	.global	_j
_j:
	.long	1
	.comm	_c,4
	.comm	_d,4
	.comm	_n,4
	.comm	_m,4
	.comm	_a,4
	.comm	_b,4
	.comm	_k,4
