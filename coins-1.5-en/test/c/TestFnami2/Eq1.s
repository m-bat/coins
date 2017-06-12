 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%eax
	cmpl	$-9,%eax
	jne	.L4
.L3:
	movl	$1,-4(%ebp)
	jmp	.L5
.L4:
	movl	$0,-4(%ebp)
.L5:
	cmpl	$-8,%eax
	jne	.L7
.L6:
	movl	$1,%edi
	jmp	.L8
.L7:
	movl	$0,%edi
.L8:
	cmpl	$-1,%eax
	jne	.L10
.L9:
	movl	$1,%esi
	jmp	.L11
.L10:
	movl	$0,%esi
.L11:
	cmpl	$0,%eax
	jne	.L13
.L12:
	movl	$1,%ebx
	jmp	.L14
.L13:
	movl	$0,%ebx
.L14:
	cmpl	$1,%eax
	jne	.L16
.L15:
	movl	$1,%edx
	jmp	.L17
.L16:
	movl	$0,%edx
.L17:
	cmpl	$20,%eax
	jne	.L19
.L18:
	movl	$1,%ecx
	jmp	.L20
.L19:
	movl	$0,%ecx
.L20:
	cmpl	$21,%eax
	jne	.L22
.L21:
	movl	$1,%eax
	jmp	.L23
.L22:
	movl	$0,%eax
.L23:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-4(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
.L24:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f1
_f1:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%eax
	cmpl	$-9,%eax
	jne	.L28
.L27:
	movl	$1,-4(%ebp)
	jmp	.L29
.L28:
	movl	$0,-4(%ebp)
.L29:
	cmpl	$-8,%eax
	jne	.L31
.L30:
	movl	$1,%edi
	jmp	.L32
.L31:
	movl	$0,%edi
.L32:
	cmpl	$-1,%eax
	jne	.L34
.L33:
	movl	$1,%esi
	jmp	.L35
.L34:
	movl	$0,%esi
.L35:
	cmpl	$0,%eax
	jne	.L37
.L36:
	movl	$1,%ebx
	jmp	.L38
.L37:
	movl	$0,%ebx
.L38:
	cmpl	$1,%eax
	jne	.L40
.L39:
	movl	$1,%edx
	jmp	.L41
.L40:
	movl	$0,%edx
.L41:
	cmpl	$20,%eax
	jne	.L43
.L42:
	movl	$1,%ecx
	jmp	.L44
.L43:
	movl	$0,%ecx
.L44:
	cmpl	$21,%eax
	jne	.L46
.L45:
	movl	$1,%eax
	jmp	.L47
.L46:
	movl	$0,%eax
.L47:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-4(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
.L48:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g0
_g0:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%eax
	cmpl	$-2147483648,%eax
	jne	.L52
.L51:
	movl	$1,%esi
	jmp	.L53
.L52:
	movl	$0,%esi
.L53:
	cmpl	$-2147483647,%eax
	jne	.L55
.L54:
	movl	$1,%ebx
	jmp	.L56
.L55:
	movl	$0,%ebx
.L56:
	cmpl	$0,%eax
	jne	.L58
.L57:
	movl	$1,%edx
	jmp	.L59
.L58:
	movl	$0,%edx
.L59:
	cmpl	$2147483646,%eax
	jne	.L61
.L60:
	movl	$1,%ecx
	jmp	.L62
.L61:
	movl	$0,%ecx
.L62:
	cmpl	$2147483647,%eax
	jne	.L64
.L63:
	movl	$1,%eax
	jmp	.L65
.L64:
	movl	$0,%eax
.L65:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
.L66:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g1
_g1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%eax
	cmpl	$-2147483648,%eax
	jne	.L70
.L69:
	movl	$1,%esi
	jmp	.L71
.L70:
	movl	$0,%esi
.L71:
	cmpl	$-2147483647,%eax
	jne	.L73
.L72:
	movl	$1,%ebx
	jmp	.L74
.L73:
	movl	$0,%ebx
.L74:
	cmpl	$0,%eax
	jne	.L76
.L75:
	movl	$1,%edx
	jmp	.L77
.L76:
	movl	$0,%edx
.L77:
	cmpl	$2147483646,%eax
	jne	.L79
.L78:
	movl	$1,%ecx
	jmp	.L80
.L79:
	movl	$0,%ecx
.L80:
	cmpl	$2147483647,%eax
	jne	.L82
.L81:
	movl	$1,%eax
	jmp	.L83
.L82:
	movl	$0,%eax
.L83:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
.L84:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_op
_op:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	cmpl	%eax,%ecx
	jne	.L88
.L87:
	movl	$1,%eax
	jmp	.L89
.L88:
	movl	$0,%eax
.L89:
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$16,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%edi
	pushl	$-9
	pushl	%edi
	call	_op
	movl	%eax,-16(%ebp)
	leal	8(%esp),%esp
	pushl	$-8
	pushl	%edi
	call	_op
	movl	%eax,-12(%ebp)
	leal	8(%esp),%esp
	pushl	$-1
	pushl	%edi
	call	_op
	movl	%eax,-8(%ebp)
	leal	8(%esp),%esp
	pushl	$0
	pushl	%edi
	call	_op
	movl	%eax,-4(%ebp)
	leal	8(%esp),%esp
	pushl	$1
	pushl	%edi
	call	_op
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	$20
	pushl	%edi
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$21
	pushl	%edi
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	-4(%ebp)
	pushl	-8(%ebp)
	pushl	-12(%ebp)
	pushl	-16(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
.L93:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g2
_g2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%edi
	pushl	$-2147483648
	pushl	%edi
	call	_op
	movl	%eax,-8(%ebp)
	leal	8(%esp),%esp
	pushl	$-2147483647
	pushl	%edi
	call	_op
	movl	%eax,-4(%ebp)
	leal	8(%esp),%esp
	pushl	$0
	pushl	%edi
	call	_op
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	$2147483646
	pushl	%edi
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$2147483647
	pushl	%edi
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	-4(%ebp)
	pushl	-8(%ebp)
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
.L96:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main1
_main1:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$84,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$-9,-28(%ebp)
	movl	$1,-84(%ebp)
	movl	-28(%ebp),%eax
	cmpl	$-8,%eax
	jne	.L103
.L102:
	movl	$1,%ecx
	jmp	.L104
.L103:
	movl	$0,%ecx
.L104:
	movl	-28(%ebp),%eax
	cmpl	$-1,%eax
	jne	.L106
.L105:
	movl	$1,%edx
	jmp	.L107
.L106:
	movl	$0,%edx
.L107:
	movl	-28(%ebp),%eax
	cmpl	$0,%eax
	jne	.L109
.L108:
	movl	$1,%ebx
	jmp	.L110
.L109:
	movl	$0,%ebx
.L110:
	movl	-28(%ebp),%eax
	cmpl	$1,%eax
	jne	.L112
.L111:
	movl	$1,%esi
	jmp	.L113
.L112:
	movl	$0,%esi
.L113:
	movl	-28(%ebp),%eax
	cmpl	$20,%eax
	jne	.L115
.L114:
	movl	$1,%edi
	jmp	.L116
.L115:
	movl	$0,%edi
.L116:
	movl	-28(%ebp),%eax
	cmpl	$21,%eax
	jne	.L118
.L117:
	movl	$1,%eax
	jmp	.L119
.L118:
	movl	$0,%eax
.L119:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-84(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	-28(%ebp),%eax
	cmpl	$-9,%eax
	jne	.L121
.L120:
	movl	$1,-56(%ebp)
	jmp	.L122
.L121:
	movl	$0,-56(%ebp)
.L122:
	movl	-28(%ebp),%eax
	cmpl	$-8,%eax
	jne	.L124
.L123:
	movl	$1,%ecx
	jmp	.L125
.L124:
	movl	$0,%ecx
.L125:
	movl	-28(%ebp),%eax
	cmpl	$-1,%eax
	jne	.L127
.L126:
	movl	$1,%edx
	jmp	.L128
.L127:
	movl	$0,%edx
.L128:
	movl	-28(%ebp),%eax
	cmpl	$0,%eax
	jne	.L130
.L129:
	movl	$1,%ebx
	jmp	.L131
.L130:
	movl	$0,%ebx
.L131:
	movl	-28(%ebp),%eax
	cmpl	$1,%eax
	jne	.L133
.L132:
	movl	$1,%esi
	jmp	.L134
.L133:
	movl	$0,%esi
.L134:
	movl	-28(%ebp),%eax
	cmpl	$20,%eax
	jne	.L136
.L135:
	movl	$1,%edi
	jmp	.L137
.L136:
	movl	$0,%edi
.L137:
	movl	-28(%ebp),%eax
	cmpl	$21,%eax
	jne	.L139
.L138:
	movl	$1,%eax
	jmp	.L140
.L139:
	movl	$0,%eax
.L140:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-56(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$-8,-24(%ebp)
	movl	$0,-80(%ebp)
	movl	-24(%ebp),%eax
	cmpl	$-8,%eax
	jne	.L145
.L144:
	movl	$1,%ecx
	jmp	.L146
.L145:
	movl	$0,%ecx
.L146:
	movl	-24(%ebp),%eax
	cmpl	$-1,%eax
	jne	.L148
.L147:
	movl	$1,%edx
	jmp	.L149
.L148:
	movl	$0,%edx
.L149:
	movl	-24(%ebp),%eax
	cmpl	$0,%eax
	jne	.L151
.L150:
	movl	$1,%ebx
	jmp	.L152
.L151:
	movl	$0,%ebx
.L152:
	movl	-24(%ebp),%eax
	cmpl	$1,%eax
	jne	.L154
.L153:
	movl	$1,%esi
	jmp	.L155
.L154:
	movl	$0,%esi
.L155:
	movl	-24(%ebp),%eax
	cmpl	$20,%eax
	jne	.L157
.L156:
	movl	$1,%edi
	jmp	.L158
.L157:
	movl	$0,%edi
.L158:
	movl	-24(%ebp),%eax
	cmpl	$21,%eax
	jne	.L160
.L159:
	movl	$1,%eax
	jmp	.L161
.L160:
	movl	$0,%eax
.L161:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-80(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	-24(%ebp),%eax
	cmpl	$-9,%eax
	jne	.L163
.L162:
	movl	$1,-52(%ebp)
	jmp	.L164
.L163:
	movl	$0,-52(%ebp)
.L164:
	movl	-24(%ebp),%eax
	cmpl	$-8,%eax
	jne	.L166
.L165:
	movl	$1,%ecx
	jmp	.L167
.L166:
	movl	$0,%ecx
.L167:
	movl	-24(%ebp),%eax
	cmpl	$-1,%eax
	jne	.L169
.L168:
	movl	$1,%edx
	jmp	.L170
.L169:
	movl	$0,%edx
.L170:
	movl	-24(%ebp),%eax
	cmpl	$0,%eax
	jne	.L172
.L171:
	movl	$1,%ebx
	jmp	.L173
.L172:
	movl	$0,%ebx
.L173:
	movl	-24(%ebp),%eax
	cmpl	$1,%eax
	jne	.L175
.L174:
	movl	$1,%esi
	jmp	.L176
.L175:
	movl	$0,%esi
.L176:
	movl	-24(%ebp),%eax
	cmpl	$20,%eax
	jne	.L178
.L177:
	movl	$1,%edi
	jmp	.L179
.L178:
	movl	$0,%edi
.L179:
	movl	-24(%ebp),%eax
	cmpl	$21,%eax
	jne	.L181
.L180:
	movl	$1,%eax
	jmp	.L182
.L181:
	movl	$0,%eax
.L182:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-52(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$-1,-20(%ebp)
	movl	$0,-76(%ebp)
	movl	-20(%ebp),%eax
	cmpl	$-8,%eax
	jne	.L187
.L186:
	movl	$1,%ecx
	jmp	.L188
.L187:
	movl	$0,%ecx
.L188:
	movl	-20(%ebp),%eax
	cmpl	$-1,%eax
	jne	.L190
.L189:
	movl	$1,%edx
	jmp	.L191
.L190:
	movl	$0,%edx
.L191:
	movl	-20(%ebp),%eax
	cmpl	$0,%eax
	jne	.L193
.L192:
	movl	$1,%ebx
	jmp	.L194
.L193:
	movl	$0,%ebx
.L194:
	movl	-20(%ebp),%eax
	cmpl	$1,%eax
	jne	.L196
.L195:
	movl	$1,%esi
	jmp	.L197
.L196:
	movl	$0,%esi
.L197:
	movl	-20(%ebp),%eax
	cmpl	$20,%eax
	jne	.L199
.L198:
	movl	$1,%edi
	jmp	.L200
.L199:
	movl	$0,%edi
.L200:
	movl	-20(%ebp),%eax
	cmpl	$21,%eax
	jne	.L202
.L201:
	movl	$1,%eax
	jmp	.L203
.L202:
	movl	$0,%eax
.L203:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-76(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	-20(%ebp),%eax
	cmpl	$-9,%eax
	jne	.L205
.L204:
	movl	$1,-48(%ebp)
	jmp	.L206
.L205:
	movl	$0,-48(%ebp)
.L206:
	movl	-20(%ebp),%eax
	cmpl	$-8,%eax
	jne	.L208
.L207:
	movl	$1,%ecx
	jmp	.L209
.L208:
	movl	$0,%ecx
.L209:
	movl	-20(%ebp),%eax
	cmpl	$-1,%eax
	jne	.L211
.L210:
	movl	$1,%edx
	jmp	.L212
.L211:
	movl	$0,%edx
.L212:
	movl	-20(%ebp),%eax
	cmpl	$0,%eax
	jne	.L214
.L213:
	movl	$1,%ebx
	jmp	.L215
.L214:
	movl	$0,%ebx
.L215:
	movl	-20(%ebp),%eax
	cmpl	$1,%eax
	jne	.L217
.L216:
	movl	$1,%esi
	jmp	.L218
.L217:
	movl	$0,%esi
.L218:
	movl	-20(%ebp),%eax
	cmpl	$20,%eax
	jne	.L220
.L219:
	movl	$1,%edi
	jmp	.L221
.L220:
	movl	$0,%edi
.L221:
	movl	-20(%ebp),%eax
	cmpl	$21,%eax
	jne	.L223
.L222:
	movl	$1,%eax
	jmp	.L224
.L223:
	movl	$0,%eax
.L224:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-48(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$0,-16(%ebp)
	movl	$0,-72(%ebp)
	movl	-16(%ebp),%eax
	cmpl	$-8,%eax
	jne	.L229
.L228:
	movl	$1,%ecx
	jmp	.L230
.L229:
	movl	$0,%ecx
.L230:
	movl	-16(%ebp),%eax
	cmpl	$-1,%eax
	jne	.L232
.L231:
	movl	$1,%edx
	jmp	.L233
.L232:
	movl	$0,%edx
.L233:
	movl	-16(%ebp),%eax
	cmpl	$0,%eax
	jne	.L235
.L234:
	movl	$1,%ebx
	jmp	.L236
.L235:
	movl	$0,%ebx
.L236:
	movl	-16(%ebp),%eax
	cmpl	$1,%eax
	jne	.L238
.L237:
	movl	$1,%esi
	jmp	.L239
.L238:
	movl	$0,%esi
.L239:
	movl	-16(%ebp),%eax
	cmpl	$20,%eax
	jne	.L241
.L240:
	movl	$1,%edi
	jmp	.L242
.L241:
	movl	$0,%edi
.L242:
	movl	-16(%ebp),%eax
	cmpl	$21,%eax
	jne	.L244
.L243:
	movl	$1,%eax
	jmp	.L245
.L244:
	movl	$0,%eax
.L245:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-72(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	-16(%ebp),%eax
	cmpl	$-9,%eax
	jne	.L247
.L246:
	movl	$1,-44(%ebp)
	jmp	.L248
.L247:
	movl	$0,-44(%ebp)
.L248:
	movl	-16(%ebp),%eax
	cmpl	$-8,%eax
	jne	.L250
.L249:
	movl	$1,%ecx
	jmp	.L251
.L250:
	movl	$0,%ecx
.L251:
	movl	-16(%ebp),%eax
	cmpl	$-1,%eax
	jne	.L253
.L252:
	movl	$1,%edx
	jmp	.L254
.L253:
	movl	$0,%edx
.L254:
	movl	-16(%ebp),%eax
	cmpl	$0,%eax
	jne	.L256
.L255:
	movl	$1,%ebx
	jmp	.L257
.L256:
	movl	$0,%ebx
.L257:
	movl	-16(%ebp),%eax
	cmpl	$1,%eax
	jne	.L259
.L258:
	movl	$1,%esi
	jmp	.L260
.L259:
	movl	$0,%esi
.L260:
	movl	-16(%ebp),%eax
	cmpl	$20,%eax
	jne	.L262
.L261:
	movl	$1,%edi
	jmp	.L263
.L262:
	movl	$0,%edi
.L263:
	movl	-16(%ebp),%eax
	cmpl	$21,%eax
	jne	.L265
.L264:
	movl	$1,%eax
	jmp	.L266
.L265:
	movl	$0,%eax
.L266:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-44(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$1,-12(%ebp)
	movl	$0,-68(%ebp)
	movl	-12(%ebp),%eax
	cmpl	$-8,%eax
	jne	.L271
.L270:
	movl	$1,%ecx
	jmp	.L272
.L271:
	movl	$0,%ecx
.L272:
	movl	-12(%ebp),%eax
	cmpl	$-1,%eax
	jne	.L274
.L273:
	movl	$1,%edx
	jmp	.L275
.L274:
	movl	$0,%edx
.L275:
	movl	-12(%ebp),%eax
	cmpl	$0,%eax
	jne	.L277
.L276:
	movl	$1,%ebx
	jmp	.L278
.L277:
	movl	$0,%ebx
.L278:
	movl	-12(%ebp),%eax
	cmpl	$1,%eax
	jne	.L280
.L279:
	movl	$1,%esi
	jmp	.L281
.L280:
	movl	$0,%esi
.L281:
	movl	-12(%ebp),%eax
	cmpl	$20,%eax
	jne	.L283
.L282:
	movl	$1,%edi
	jmp	.L284
.L283:
	movl	$0,%edi
.L284:
	movl	-12(%ebp),%eax
	cmpl	$21,%eax
	jne	.L286
.L285:
	movl	$1,%eax
	jmp	.L287
.L286:
	movl	$0,%eax
.L287:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-68(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	-12(%ebp),%eax
	cmpl	$-9,%eax
	jne	.L289
.L288:
	movl	$1,-40(%ebp)
	jmp	.L290
.L289:
	movl	$0,-40(%ebp)
.L290:
	movl	-12(%ebp),%eax
	cmpl	$-8,%eax
	jne	.L292
.L291:
	movl	$1,%ecx
	jmp	.L293
.L292:
	movl	$0,%ecx
.L293:
	movl	-12(%ebp),%eax
	cmpl	$-1,%eax
	jne	.L295
.L294:
	movl	$1,%edx
	jmp	.L296
.L295:
	movl	$0,%edx
.L296:
	movl	-12(%ebp),%eax
	cmpl	$0,%eax
	jne	.L298
.L297:
	movl	$1,%ebx
	jmp	.L299
.L298:
	movl	$0,%ebx
.L299:
	movl	-12(%ebp),%eax
	cmpl	$1,%eax
	jne	.L301
.L300:
	movl	$1,%esi
	jmp	.L302
.L301:
	movl	$0,%esi
.L302:
	movl	-12(%ebp),%eax
	cmpl	$20,%eax
	jne	.L304
.L303:
	movl	$1,%edi
	jmp	.L305
.L304:
	movl	$0,%edi
.L305:
	movl	-12(%ebp),%eax
	cmpl	$21,%eax
	jne	.L307
.L306:
	movl	$1,%eax
	jmp	.L308
.L307:
	movl	$0,%eax
.L308:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-40(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$20,-8(%ebp)
	movl	$0,-64(%ebp)
	movl	-8(%ebp),%eax
	cmpl	$-8,%eax
	jne	.L313
.L312:
	movl	$1,%ecx
	jmp	.L314
.L313:
	movl	$0,%ecx
.L314:
	movl	-8(%ebp),%eax
	cmpl	$-1,%eax
	jne	.L316
.L315:
	movl	$1,%edx
	jmp	.L317
.L316:
	movl	$0,%edx
.L317:
	movl	-8(%ebp),%eax
	cmpl	$0,%eax
	jne	.L319
.L318:
	movl	$1,%ebx
	jmp	.L320
.L319:
	movl	$0,%ebx
.L320:
	movl	-8(%ebp),%eax
	cmpl	$1,%eax
	jne	.L322
.L321:
	movl	$1,%esi
	jmp	.L323
.L322:
	movl	$0,%esi
.L323:
	movl	-8(%ebp),%eax
	cmpl	$20,%eax
	jne	.L325
.L324:
	movl	$1,%edi
	jmp	.L326
.L325:
	movl	$0,%edi
.L326:
	movl	-8(%ebp),%eax
	cmpl	$21,%eax
	jne	.L328
.L327:
	movl	$1,%eax
	jmp	.L329
.L328:
	movl	$0,%eax
.L329:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-64(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	-8(%ebp),%eax
	cmpl	$-9,%eax
	jne	.L331
.L330:
	movl	$1,-36(%ebp)
	jmp	.L332
.L331:
	movl	$0,-36(%ebp)
.L332:
	movl	-8(%ebp),%eax
	cmpl	$-8,%eax
	jne	.L334
.L333:
	movl	$1,%ecx
	jmp	.L335
.L334:
	movl	$0,%ecx
.L335:
	movl	-8(%ebp),%eax
	cmpl	$-1,%eax
	jne	.L337
.L336:
	movl	$1,%edx
	jmp	.L338
.L337:
	movl	$0,%edx
.L338:
	movl	-8(%ebp),%eax
	cmpl	$0,%eax
	jne	.L340
.L339:
	movl	$1,%ebx
	jmp	.L341
.L340:
	movl	$0,%ebx
.L341:
	movl	-8(%ebp),%eax
	cmpl	$1,%eax
	jne	.L343
.L342:
	movl	$1,%esi
	jmp	.L344
.L343:
	movl	$0,%esi
.L344:
	movl	-8(%ebp),%eax
	cmpl	$20,%eax
	jne	.L346
.L345:
	movl	$1,%edi
	jmp	.L347
.L346:
	movl	$0,%edi
.L347:
	movl	-8(%ebp),%eax
	cmpl	$21,%eax
	jne	.L349
.L348:
	movl	$1,%eax
	jmp	.L350
.L349:
	movl	$0,%eax
.L350:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-36(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$21,-4(%ebp)
	movl	$0,-60(%ebp)
	movl	-4(%ebp),%eax
	cmpl	$-8,%eax
	jne	.L355
.L354:
	movl	$1,%ecx
	jmp	.L356
.L355:
	movl	$0,%ecx
.L356:
	movl	-4(%ebp),%eax
	cmpl	$-1,%eax
	jne	.L358
.L357:
	movl	$1,%edx
	jmp	.L359
.L358:
	movl	$0,%edx
.L359:
	movl	-4(%ebp),%eax
	cmpl	$0,%eax
	jne	.L361
.L360:
	movl	$1,%ebx
	jmp	.L362
.L361:
	movl	$0,%ebx
.L362:
	movl	-4(%ebp),%eax
	cmpl	$1,%eax
	jne	.L364
.L363:
	movl	$1,%esi
	jmp	.L365
.L364:
	movl	$0,%esi
.L365:
	movl	-4(%ebp),%eax
	cmpl	$20,%eax
	jne	.L367
.L366:
	movl	$1,%edi
	jmp	.L368
.L367:
	movl	$0,%edi
.L368:
	movl	-4(%ebp),%eax
	cmpl	$21,%eax
	jne	.L370
.L369:
	movl	$1,%eax
	jmp	.L371
.L370:
	movl	$0,%eax
.L371:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-60(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	-4(%ebp),%eax
	cmpl	$-9,%eax
	jne	.L373
.L372:
	movl	$1,-32(%ebp)
	jmp	.L374
.L373:
	movl	$0,-32(%ebp)
.L374:
	movl	-4(%ebp),%eax
	cmpl	$-8,%eax
	jne	.L376
.L375:
	movl	$1,%ecx
	jmp	.L377
.L376:
	movl	$0,%ecx
.L377:
	movl	-4(%ebp),%eax
	cmpl	$-1,%eax
	jne	.L379
.L378:
	movl	$1,%edx
	jmp	.L380
.L379:
	movl	$0,%edx
.L380:
	movl	-4(%ebp),%eax
	cmpl	$0,%eax
	jne	.L382
.L381:
	movl	$1,%ebx
	jmp	.L383
.L382:
	movl	$0,%ebx
.L383:
	movl	-4(%ebp),%eax
	cmpl	$1,%eax
	jne	.L385
.L384:
	movl	$1,%esi
	jmp	.L386
.L385:
	movl	$0,%esi
.L386:
	movl	-4(%ebp),%eax
	cmpl	$20,%eax
	jne	.L388
.L387:
	movl	$1,%edi
	jmp	.L389
.L388:
	movl	$0,%edi
.L389:
	movl	-4(%ebp),%eax
	cmpl	$21,%eax
	jne	.L391
.L390:
	movl	$1,%eax
	jmp	.L392
.L391:
	movl	$0,%eax
.L392:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-32(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
.L393:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main2
_main2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$-2147483648,%ebx
	movl	$1,%edi
	cmpl	$-2147483647,%ebx
	jne	.L400
.L399:
	movl	$1,%esi
	jmp	.L401
.L400:
	movl	$0,%esi
.L401:
	cmpl	$0,%ebx
	jne	.L403
.L402:
	movl	$1,%edx
	jmp	.L404
.L403:
	movl	$0,%edx
.L404:
	cmpl	$2147483646,%ebx
	jne	.L406
.L405:
	movl	$1,%ecx
	jmp	.L407
.L406:
	movl	$0,%ecx
.L407:
	cmpl	$2147483647,%ebx
	jne	.L409
.L408:
	movl	$1,%eax
	jmp	.L410
.L409:
	movl	$0,%eax
.L410:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	cmpl	$-2147483648,%ebx
	jne	.L412
.L411:
	movl	$1,%edi
	jmp	.L413
.L412:
	movl	$0,%edi
.L413:
	cmpl	$-2147483647,%ebx
	jne	.L415
.L414:
	movl	$1,%esi
	jmp	.L416
.L415:
	movl	$0,%esi
.L416:
	cmpl	$0,%ebx
	jne	.L418
.L417:
	movl	$1,%edx
	jmp	.L419
.L418:
	movl	$0,%edx
.L419:
	cmpl	$2147483646,%ebx
	jne	.L421
.L420:
	movl	$1,%ecx
	jmp	.L422
.L421:
	movl	$0,%ecx
.L422:
	cmpl	$2147483647,%ebx
	jne	.L424
.L423:
	movl	$1,%eax
	jmp	.L425
.L424:
	movl	$0,%eax
.L425:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	movl	$-2147483647,%ebx
	movl	$0,%edi
	cmpl	$-2147483647,%ebx
	jne	.L430
.L429:
	movl	$1,%esi
	jmp	.L431
.L430:
	movl	$0,%esi
.L431:
	cmpl	$0,%ebx
	jne	.L433
.L432:
	movl	$1,%edx
	jmp	.L434
.L433:
	movl	$0,%edx
.L434:
	cmpl	$2147483646,%ebx
	jne	.L436
.L435:
	movl	$1,%ecx
	jmp	.L437
.L436:
	movl	$0,%ecx
.L437:
	cmpl	$2147483647,%ebx
	jne	.L439
.L438:
	movl	$1,%eax
	jmp	.L440
.L439:
	movl	$0,%eax
.L440:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	cmpl	$-2147483648,%ebx
	jne	.L442
.L441:
	movl	$1,%edi
	jmp	.L443
.L442:
	movl	$0,%edi
.L443:
	cmpl	$-2147483647,%ebx
	jne	.L445
.L444:
	movl	$1,%esi
	jmp	.L446
.L445:
	movl	$0,%esi
.L446:
	cmpl	$0,%ebx
	jne	.L448
.L447:
	movl	$1,%edx
	jmp	.L449
.L448:
	movl	$0,%edx
.L449:
	cmpl	$2147483646,%ebx
	jne	.L451
.L450:
	movl	$1,%ecx
	jmp	.L452
.L451:
	movl	$0,%ecx
.L452:
	cmpl	$2147483647,%ebx
	jne	.L454
.L453:
	movl	$1,%eax
	jmp	.L455
.L454:
	movl	$0,%eax
.L455:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	movl	$0,%ebx
	movl	$0,%edi
	cmpl	$-2147483647,%ebx
	jne	.L460
.L459:
	movl	$1,%esi
	jmp	.L461
.L460:
	movl	$0,%esi
.L461:
	cmpl	$0,%ebx
	jne	.L463
.L462:
	movl	$1,%edx
	jmp	.L464
.L463:
	movl	$0,%edx
.L464:
	cmpl	$2147483646,%ebx
	jne	.L466
.L465:
	movl	$1,%ecx
	jmp	.L467
.L466:
	movl	$0,%ecx
.L467:
	cmpl	$2147483647,%ebx
	jne	.L469
.L468:
	movl	$1,%eax
	jmp	.L470
.L469:
	movl	$0,%eax
.L470:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	cmpl	$-2147483648,%ebx
	jne	.L472
.L471:
	movl	$1,%edi
	jmp	.L473
.L472:
	movl	$0,%edi
.L473:
	cmpl	$-2147483647,%ebx
	jne	.L475
.L474:
	movl	$1,%esi
	jmp	.L476
.L475:
	movl	$0,%esi
.L476:
	cmpl	$0,%ebx
	jne	.L478
.L477:
	movl	$1,%edx
	jmp	.L479
.L478:
	movl	$0,%edx
.L479:
	cmpl	$2147483646,%ebx
	jne	.L481
.L480:
	movl	$1,%ecx
	jmp	.L482
.L481:
	movl	$0,%ecx
.L482:
	cmpl	$2147483647,%ebx
	jne	.L484
.L483:
	movl	$1,%eax
	jmp	.L485
.L484:
	movl	$0,%eax
.L485:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	movl	$2147483646,%ebx
	movl	$0,%edi
	cmpl	$-2147483647,%ebx
	jne	.L490
.L489:
	movl	$1,%esi
	jmp	.L491
.L490:
	movl	$0,%esi
.L491:
	cmpl	$0,%ebx
	jne	.L493
.L492:
	movl	$1,%edx
	jmp	.L494
.L493:
	movl	$0,%edx
.L494:
	cmpl	$2147483646,%ebx
	jne	.L496
.L495:
	movl	$1,%ecx
	jmp	.L497
.L496:
	movl	$0,%ecx
.L497:
	cmpl	$2147483647,%ebx
	jne	.L499
.L498:
	movl	$1,%eax
	jmp	.L500
.L499:
	movl	$0,%eax
.L500:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	cmpl	$-2147483648,%ebx
	jne	.L502
.L501:
	movl	$1,%edi
	jmp	.L503
.L502:
	movl	$0,%edi
.L503:
	cmpl	$-2147483647,%ebx
	jne	.L505
.L504:
	movl	$1,%esi
	jmp	.L506
.L505:
	movl	$0,%esi
.L506:
	cmpl	$0,%ebx
	jne	.L508
.L507:
	movl	$1,%edx
	jmp	.L509
.L508:
	movl	$0,%edx
.L509:
	cmpl	$2147483646,%ebx
	jne	.L511
.L510:
	movl	$1,%ecx
	jmp	.L512
.L511:
	movl	$0,%ecx
.L512:
	cmpl	$2147483647,%ebx
	jne	.L514
.L513:
	movl	$1,%eax
	jmp	.L515
.L514:
	movl	$0,%eax
.L515:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	movl	$2147483647,%ebx
	movl	$0,%edi
	cmpl	$-2147483647,%ebx
	jne	.L520
.L519:
	movl	$1,%esi
	jmp	.L521
.L520:
	movl	$0,%esi
.L521:
	cmpl	$0,%ebx
	jne	.L523
.L522:
	movl	$1,%edx
	jmp	.L524
.L523:
	movl	$0,%edx
.L524:
	cmpl	$2147483646,%ebx
	jne	.L526
.L525:
	movl	$1,%ecx
	jmp	.L527
.L526:
	movl	$0,%ecx
.L527:
	cmpl	$2147483647,%ebx
	jne	.L529
.L528:
	movl	$1,%eax
	jmp	.L530
.L529:
	movl	$0,%eax
.L530:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	cmpl	$-2147483648,%ebx
	jne	.L532
.L531:
	movl	$1,%edi
	jmp	.L533
.L532:
	movl	$0,%edi
.L533:
	cmpl	$-2147483647,%ebx
	jne	.L535
.L534:
	movl	$1,%esi
	jmp	.L536
.L535:
	movl	$0,%esi
.L536:
	cmpl	$0,%ebx
	jne	.L538
.L537:
	movl	$1,%edx
	jmp	.L539
.L538:
	movl	$0,%edx
.L539:
	cmpl	$2147483646,%ebx
	jne	.L541
.L540:
	movl	$1,%ecx
	jmp	.L542
.L541:
	movl	$0,%ecx
.L542:
	cmpl	$2147483647,%ebx
	jne	.L544
.L543:
	movl	$1,%eax
	jmp	.L545
.L544:
	movl	$0,%eax
.L545:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
.L546:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main3
_main3:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$84,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$-9,-28(%ebp)
	movl	$1,-84(%ebp)
	movl	$-8,%eax
	cmpl	-28(%ebp),%eax
	jne	.L553
.L552:
	movl	$1,%ecx
	jmp	.L554
.L553:
	movl	$0,%ecx
.L554:
	movl	$-1,%eax
	cmpl	-28(%ebp),%eax
	jne	.L556
.L555:
	movl	$1,%edx
	jmp	.L557
.L556:
	movl	$0,%edx
.L557:
	movl	$0,%eax
	cmpl	-28(%ebp),%eax
	jne	.L559
.L558:
	movl	$1,%ebx
	jmp	.L560
.L559:
	movl	$0,%ebx
.L560:
	movl	$1,%eax
	cmpl	-28(%ebp),%eax
	jne	.L562
.L561:
	movl	$1,%esi
	jmp	.L563
.L562:
	movl	$0,%esi
.L563:
	movl	$20,%eax
	cmpl	-28(%ebp),%eax
	jne	.L565
.L564:
	movl	$1,%edi
	jmp	.L566
.L565:
	movl	$0,%edi
.L566:
	movl	$21,%eax
	cmpl	-28(%ebp),%eax
	jne	.L568
.L567:
	movl	$1,%eax
	jmp	.L569
.L568:
	movl	$0,%eax
.L569:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-84(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$-9,%eax
	cmpl	-28(%ebp),%eax
	jne	.L571
.L570:
	movl	$1,-56(%ebp)
	jmp	.L572
.L571:
	movl	$0,-56(%ebp)
.L572:
	movl	$-8,%eax
	cmpl	-28(%ebp),%eax
	jne	.L574
.L573:
	movl	$1,%ecx
	jmp	.L575
.L574:
	movl	$0,%ecx
.L575:
	movl	$-1,%eax
	cmpl	-28(%ebp),%eax
	jne	.L577
.L576:
	movl	$1,%edx
	jmp	.L578
.L577:
	movl	$0,%edx
.L578:
	movl	$0,%eax
	cmpl	-28(%ebp),%eax
	jne	.L580
.L579:
	movl	$1,%ebx
	jmp	.L581
.L580:
	movl	$0,%ebx
.L581:
	movl	$1,%eax
	cmpl	-28(%ebp),%eax
	jne	.L583
.L582:
	movl	$1,%esi
	jmp	.L584
.L583:
	movl	$0,%esi
.L584:
	movl	$20,%eax
	cmpl	-28(%ebp),%eax
	jne	.L586
.L585:
	movl	$1,%edi
	jmp	.L587
.L586:
	movl	$0,%edi
.L587:
	movl	$21,%eax
	cmpl	-28(%ebp),%eax
	jne	.L589
.L588:
	movl	$1,%eax
	jmp	.L590
.L589:
	movl	$0,%eax
.L590:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-56(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$-8,-24(%ebp)
	movl	$0,-80(%ebp)
	movl	$-8,%eax
	cmpl	-24(%ebp),%eax
	jne	.L595
.L594:
	movl	$1,%ecx
	jmp	.L596
.L595:
	movl	$0,%ecx
.L596:
	movl	$-1,%eax
	cmpl	-24(%ebp),%eax
	jne	.L598
.L597:
	movl	$1,%edx
	jmp	.L599
.L598:
	movl	$0,%edx
.L599:
	movl	$0,%eax
	cmpl	-24(%ebp),%eax
	jne	.L601
.L600:
	movl	$1,%ebx
	jmp	.L602
.L601:
	movl	$0,%ebx
.L602:
	movl	$1,%eax
	cmpl	-24(%ebp),%eax
	jne	.L604
.L603:
	movl	$1,%esi
	jmp	.L605
.L604:
	movl	$0,%esi
.L605:
	movl	$20,%eax
	cmpl	-24(%ebp),%eax
	jne	.L607
.L606:
	movl	$1,%edi
	jmp	.L608
.L607:
	movl	$0,%edi
.L608:
	movl	$21,%eax
	cmpl	-24(%ebp),%eax
	jne	.L610
.L609:
	movl	$1,%eax
	jmp	.L611
.L610:
	movl	$0,%eax
.L611:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-80(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$-9,%eax
	cmpl	-24(%ebp),%eax
	jne	.L613
.L612:
	movl	$1,-52(%ebp)
	jmp	.L614
.L613:
	movl	$0,-52(%ebp)
.L614:
	movl	$-8,%eax
	cmpl	-24(%ebp),%eax
	jne	.L616
.L615:
	movl	$1,%ecx
	jmp	.L617
.L616:
	movl	$0,%ecx
.L617:
	movl	$-1,%eax
	cmpl	-24(%ebp),%eax
	jne	.L619
.L618:
	movl	$1,%edx
	jmp	.L620
.L619:
	movl	$0,%edx
.L620:
	movl	$0,%eax
	cmpl	-24(%ebp),%eax
	jne	.L622
.L621:
	movl	$1,%ebx
	jmp	.L623
.L622:
	movl	$0,%ebx
.L623:
	movl	$1,%eax
	cmpl	-24(%ebp),%eax
	jne	.L625
.L624:
	movl	$1,%esi
	jmp	.L626
.L625:
	movl	$0,%esi
.L626:
	movl	$20,%eax
	cmpl	-24(%ebp),%eax
	jne	.L628
.L627:
	movl	$1,%edi
	jmp	.L629
.L628:
	movl	$0,%edi
.L629:
	movl	$21,%eax
	cmpl	-24(%ebp),%eax
	jne	.L631
.L630:
	movl	$1,%eax
	jmp	.L632
.L631:
	movl	$0,%eax
.L632:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-52(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$-1,-20(%ebp)
	movl	$0,-76(%ebp)
	movl	$-8,%eax
	cmpl	-20(%ebp),%eax
	jne	.L637
.L636:
	movl	$1,%ecx
	jmp	.L638
.L637:
	movl	$0,%ecx
.L638:
	movl	$-1,%eax
	cmpl	-20(%ebp),%eax
	jne	.L640
.L639:
	movl	$1,%edx
	jmp	.L641
.L640:
	movl	$0,%edx
.L641:
	movl	$0,%eax
	cmpl	-20(%ebp),%eax
	jne	.L643
.L642:
	movl	$1,%ebx
	jmp	.L644
.L643:
	movl	$0,%ebx
.L644:
	movl	$1,%eax
	cmpl	-20(%ebp),%eax
	jne	.L646
.L645:
	movl	$1,%esi
	jmp	.L647
.L646:
	movl	$0,%esi
.L647:
	movl	$20,%eax
	cmpl	-20(%ebp),%eax
	jne	.L649
.L648:
	movl	$1,%edi
	jmp	.L650
.L649:
	movl	$0,%edi
.L650:
	movl	$21,%eax
	cmpl	-20(%ebp),%eax
	jne	.L652
.L651:
	movl	$1,%eax
	jmp	.L653
.L652:
	movl	$0,%eax
.L653:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-76(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$-9,%eax
	cmpl	-20(%ebp),%eax
	jne	.L655
.L654:
	movl	$1,-48(%ebp)
	jmp	.L656
.L655:
	movl	$0,-48(%ebp)
.L656:
	movl	$-8,%eax
	cmpl	-20(%ebp),%eax
	jne	.L658
.L657:
	movl	$1,%ecx
	jmp	.L659
.L658:
	movl	$0,%ecx
.L659:
	movl	$-1,%eax
	cmpl	-20(%ebp),%eax
	jne	.L661
.L660:
	movl	$1,%edx
	jmp	.L662
.L661:
	movl	$0,%edx
.L662:
	movl	$0,%eax
	cmpl	-20(%ebp),%eax
	jne	.L664
.L663:
	movl	$1,%ebx
	jmp	.L665
.L664:
	movl	$0,%ebx
.L665:
	movl	$1,%eax
	cmpl	-20(%ebp),%eax
	jne	.L667
.L666:
	movl	$1,%esi
	jmp	.L668
.L667:
	movl	$0,%esi
.L668:
	movl	$20,%eax
	cmpl	-20(%ebp),%eax
	jne	.L670
.L669:
	movl	$1,%edi
	jmp	.L671
.L670:
	movl	$0,%edi
.L671:
	movl	$21,%eax
	cmpl	-20(%ebp),%eax
	jne	.L673
.L672:
	movl	$1,%eax
	jmp	.L674
.L673:
	movl	$0,%eax
.L674:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-48(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$0,-16(%ebp)
	movl	$0,-72(%ebp)
	movl	$-8,%eax
	cmpl	-16(%ebp),%eax
	jne	.L679
.L678:
	movl	$1,%ecx
	jmp	.L680
.L679:
	movl	$0,%ecx
.L680:
	movl	$-1,%eax
	cmpl	-16(%ebp),%eax
	jne	.L682
.L681:
	movl	$1,%edx
	jmp	.L683
.L682:
	movl	$0,%edx
.L683:
	movl	$0,%eax
	cmpl	-16(%ebp),%eax
	jne	.L685
.L684:
	movl	$1,%ebx
	jmp	.L686
.L685:
	movl	$0,%ebx
.L686:
	movl	$1,%eax
	cmpl	-16(%ebp),%eax
	jne	.L688
.L687:
	movl	$1,%esi
	jmp	.L689
.L688:
	movl	$0,%esi
.L689:
	movl	$20,%eax
	cmpl	-16(%ebp),%eax
	jne	.L691
.L690:
	movl	$1,%edi
	jmp	.L692
.L691:
	movl	$0,%edi
.L692:
	movl	$21,%eax
	cmpl	-16(%ebp),%eax
	jne	.L694
.L693:
	movl	$1,%eax
	jmp	.L695
.L694:
	movl	$0,%eax
.L695:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-72(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$-9,%eax
	cmpl	-16(%ebp),%eax
	jne	.L697
.L696:
	movl	$1,-44(%ebp)
	jmp	.L698
.L697:
	movl	$0,-44(%ebp)
.L698:
	movl	$-8,%eax
	cmpl	-16(%ebp),%eax
	jne	.L700
.L699:
	movl	$1,%ecx
	jmp	.L701
.L700:
	movl	$0,%ecx
.L701:
	movl	$-1,%eax
	cmpl	-16(%ebp),%eax
	jne	.L703
.L702:
	movl	$1,%edx
	jmp	.L704
.L703:
	movl	$0,%edx
.L704:
	movl	$0,%eax
	cmpl	-16(%ebp),%eax
	jne	.L706
.L705:
	movl	$1,%ebx
	jmp	.L707
.L706:
	movl	$0,%ebx
.L707:
	movl	$1,%eax
	cmpl	-16(%ebp),%eax
	jne	.L709
.L708:
	movl	$1,%esi
	jmp	.L710
.L709:
	movl	$0,%esi
.L710:
	movl	$20,%eax
	cmpl	-16(%ebp),%eax
	jne	.L712
.L711:
	movl	$1,%edi
	jmp	.L713
.L712:
	movl	$0,%edi
.L713:
	movl	$21,%eax
	cmpl	-16(%ebp),%eax
	jne	.L715
.L714:
	movl	$1,%eax
	jmp	.L716
.L715:
	movl	$0,%eax
.L716:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-44(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$1,-12(%ebp)
	movl	$0,-68(%ebp)
	movl	$-8,%eax
	cmpl	-12(%ebp),%eax
	jne	.L721
.L720:
	movl	$1,%ecx
	jmp	.L722
.L721:
	movl	$0,%ecx
.L722:
	movl	$-1,%eax
	cmpl	-12(%ebp),%eax
	jne	.L724
.L723:
	movl	$1,%edx
	jmp	.L725
.L724:
	movl	$0,%edx
.L725:
	movl	$0,%eax
	cmpl	-12(%ebp),%eax
	jne	.L727
.L726:
	movl	$1,%ebx
	jmp	.L728
.L727:
	movl	$0,%ebx
.L728:
	movl	$1,%eax
	cmpl	-12(%ebp),%eax
	jne	.L730
.L729:
	movl	$1,%esi
	jmp	.L731
.L730:
	movl	$0,%esi
.L731:
	movl	$20,%eax
	cmpl	-12(%ebp),%eax
	jne	.L733
.L732:
	movl	$1,%edi
	jmp	.L734
.L733:
	movl	$0,%edi
.L734:
	movl	$21,%eax
	cmpl	-12(%ebp),%eax
	jne	.L736
.L735:
	movl	$1,%eax
	jmp	.L737
.L736:
	movl	$0,%eax
.L737:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-68(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$-9,%eax
	cmpl	-12(%ebp),%eax
	jne	.L739
.L738:
	movl	$1,-40(%ebp)
	jmp	.L740
.L739:
	movl	$0,-40(%ebp)
.L740:
	movl	$-8,%eax
	cmpl	-12(%ebp),%eax
	jne	.L742
.L741:
	movl	$1,%ecx
	jmp	.L743
.L742:
	movl	$0,%ecx
.L743:
	movl	$-1,%eax
	cmpl	-12(%ebp),%eax
	jne	.L745
.L744:
	movl	$1,%edx
	jmp	.L746
.L745:
	movl	$0,%edx
.L746:
	movl	$0,%eax
	cmpl	-12(%ebp),%eax
	jne	.L748
.L747:
	movl	$1,%ebx
	jmp	.L749
.L748:
	movl	$0,%ebx
.L749:
	movl	$1,%eax
	cmpl	-12(%ebp),%eax
	jne	.L751
.L750:
	movl	$1,%esi
	jmp	.L752
.L751:
	movl	$0,%esi
.L752:
	movl	$20,%eax
	cmpl	-12(%ebp),%eax
	jne	.L754
.L753:
	movl	$1,%edi
	jmp	.L755
.L754:
	movl	$0,%edi
.L755:
	movl	$21,%eax
	cmpl	-12(%ebp),%eax
	jne	.L757
.L756:
	movl	$1,%eax
	jmp	.L758
.L757:
	movl	$0,%eax
.L758:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-40(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$20,-8(%ebp)
	movl	$0,-64(%ebp)
	movl	$-8,%eax
	cmpl	-8(%ebp),%eax
	jne	.L763
.L762:
	movl	$1,%ecx
	jmp	.L764
.L763:
	movl	$0,%ecx
.L764:
	movl	$-1,%eax
	cmpl	-8(%ebp),%eax
	jne	.L766
.L765:
	movl	$1,%edx
	jmp	.L767
.L766:
	movl	$0,%edx
.L767:
	movl	$0,%eax
	cmpl	-8(%ebp),%eax
	jne	.L769
.L768:
	movl	$1,%ebx
	jmp	.L770
.L769:
	movl	$0,%ebx
.L770:
	movl	$1,%eax
	cmpl	-8(%ebp),%eax
	jne	.L772
.L771:
	movl	$1,%esi
	jmp	.L773
.L772:
	movl	$0,%esi
.L773:
	movl	$20,%eax
	cmpl	-8(%ebp),%eax
	jne	.L775
.L774:
	movl	$1,%edi
	jmp	.L776
.L775:
	movl	$0,%edi
.L776:
	movl	$21,%eax
	cmpl	-8(%ebp),%eax
	jne	.L778
.L777:
	movl	$1,%eax
	jmp	.L779
.L778:
	movl	$0,%eax
.L779:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-64(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$-9,%eax
	cmpl	-8(%ebp),%eax
	jne	.L781
.L780:
	movl	$1,-36(%ebp)
	jmp	.L782
.L781:
	movl	$0,-36(%ebp)
.L782:
	movl	$-8,%eax
	cmpl	-8(%ebp),%eax
	jne	.L784
.L783:
	movl	$1,%ecx
	jmp	.L785
.L784:
	movl	$0,%ecx
.L785:
	movl	$-1,%eax
	cmpl	-8(%ebp),%eax
	jne	.L787
.L786:
	movl	$1,%edx
	jmp	.L788
.L787:
	movl	$0,%edx
.L788:
	movl	$0,%eax
	cmpl	-8(%ebp),%eax
	jne	.L790
.L789:
	movl	$1,%ebx
	jmp	.L791
.L790:
	movl	$0,%ebx
.L791:
	movl	$1,%eax
	cmpl	-8(%ebp),%eax
	jne	.L793
.L792:
	movl	$1,%esi
	jmp	.L794
.L793:
	movl	$0,%esi
.L794:
	movl	$20,%eax
	cmpl	-8(%ebp),%eax
	jne	.L796
.L795:
	movl	$1,%edi
	jmp	.L797
.L796:
	movl	$0,%edi
.L797:
	movl	$21,%eax
	cmpl	-8(%ebp),%eax
	jne	.L799
.L798:
	movl	$1,%eax
	jmp	.L800
.L799:
	movl	$0,%eax
.L800:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-36(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$21,-4(%ebp)
	movl	$0,-60(%ebp)
	movl	$-8,%eax
	cmpl	-4(%ebp),%eax
	jne	.L805
.L804:
	movl	$1,%ecx
	jmp	.L806
.L805:
	movl	$0,%ecx
.L806:
	movl	$-1,%eax
	cmpl	-4(%ebp),%eax
	jne	.L808
.L807:
	movl	$1,%edx
	jmp	.L809
.L808:
	movl	$0,%edx
.L809:
	movl	$0,%eax
	cmpl	-4(%ebp),%eax
	jne	.L811
.L810:
	movl	$1,%ebx
	jmp	.L812
.L811:
	movl	$0,%ebx
.L812:
	movl	$1,%eax
	cmpl	-4(%ebp),%eax
	jne	.L814
.L813:
	movl	$1,%esi
	jmp	.L815
.L814:
	movl	$0,%esi
.L815:
	movl	$20,%eax
	cmpl	-4(%ebp),%eax
	jne	.L817
.L816:
	movl	$1,%edi
	jmp	.L818
.L817:
	movl	$0,%edi
.L818:
	movl	$21,%eax
	cmpl	-4(%ebp),%eax
	jne	.L820
.L819:
	movl	$1,%eax
	jmp	.L821
.L820:
	movl	$0,%eax
.L821:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-60(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	movl	$-9,%eax
	cmpl	-4(%ebp),%eax
	jne	.L823
.L822:
	movl	$1,-32(%ebp)
	jmp	.L824
.L823:
	movl	$0,-32(%ebp)
.L824:
	movl	$-8,%eax
	cmpl	-4(%ebp),%eax
	jne	.L826
.L825:
	movl	$1,%ecx
	jmp	.L827
.L826:
	movl	$0,%ecx
.L827:
	movl	$-1,%eax
	cmpl	-4(%ebp),%eax
	jne	.L829
.L828:
	movl	$1,%edx
	jmp	.L830
.L829:
	movl	$0,%edx
.L830:
	movl	$0,%eax
	cmpl	-4(%ebp),%eax
	jne	.L832
.L831:
	movl	$1,%ebx
	jmp	.L833
.L832:
	movl	$0,%ebx
.L833:
	movl	$1,%eax
	cmpl	-4(%ebp),%eax
	jne	.L835
.L834:
	movl	$1,%esi
	jmp	.L836
.L835:
	movl	$0,%esi
.L836:
	movl	$20,%eax
	cmpl	-4(%ebp),%eax
	jne	.L838
.L837:
	movl	$1,%edi
	jmp	.L839
.L838:
	movl	$0,%edi
.L839:
	movl	$21,%eax
	cmpl	-4(%ebp),%eax
	jne	.L841
.L840:
	movl	$1,%eax
	jmp	.L842
.L841:
	movl	$0,%eax
.L842:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-32(%ebp)
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
.L843:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main4
_main4:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$-2147483648,%ebx
	movl	$1,%edi
	cmpl	$-2147483647,%ebx
	jne	.L850
.L849:
	movl	$1,%esi
	jmp	.L851
.L850:
	movl	$0,%esi
.L851:
	cmpl	$0,%ebx
	jne	.L853
.L852:
	movl	$1,%edx
	jmp	.L854
.L853:
	movl	$0,%edx
.L854:
	cmpl	$2147483646,%ebx
	jne	.L856
.L855:
	movl	$1,%ecx
	jmp	.L857
.L856:
	movl	$0,%ecx
.L857:
	cmpl	$2147483647,%ebx
	jne	.L859
.L858:
	movl	$1,%eax
	jmp	.L860
.L859:
	movl	$0,%eax
.L860:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	cmpl	$-2147483648,%ebx
	jne	.L862
.L861:
	movl	$1,%edi
	jmp	.L863
.L862:
	movl	$0,%edi
.L863:
	cmpl	$-2147483647,%ebx
	jne	.L865
.L864:
	movl	$1,%esi
	jmp	.L866
.L865:
	movl	$0,%esi
.L866:
	cmpl	$0,%ebx
	jne	.L868
.L867:
	movl	$1,%edx
	jmp	.L869
.L868:
	movl	$0,%edx
.L869:
	cmpl	$2147483646,%ebx
	jne	.L871
.L870:
	movl	$1,%ecx
	jmp	.L872
.L871:
	movl	$0,%ecx
.L872:
	cmpl	$2147483647,%ebx
	jne	.L874
.L873:
	movl	$1,%eax
	jmp	.L875
.L874:
	movl	$0,%eax
.L875:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	movl	$-2147483647,%ebx
	movl	$0,%edi
	cmpl	$-2147483647,%ebx
	jne	.L880
.L879:
	movl	$1,%esi
	jmp	.L881
.L880:
	movl	$0,%esi
.L881:
	cmpl	$0,%ebx
	jne	.L883
.L882:
	movl	$1,%edx
	jmp	.L884
.L883:
	movl	$0,%edx
.L884:
	cmpl	$2147483646,%ebx
	jne	.L886
.L885:
	movl	$1,%ecx
	jmp	.L887
.L886:
	movl	$0,%ecx
.L887:
	cmpl	$2147483647,%ebx
	jne	.L889
.L888:
	movl	$1,%eax
	jmp	.L890
.L889:
	movl	$0,%eax
.L890:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	cmpl	$-2147483648,%ebx
	jne	.L892
.L891:
	movl	$1,%edi
	jmp	.L893
.L892:
	movl	$0,%edi
.L893:
	cmpl	$-2147483647,%ebx
	jne	.L895
.L894:
	movl	$1,%esi
	jmp	.L896
.L895:
	movl	$0,%esi
.L896:
	cmpl	$0,%ebx
	jne	.L898
.L897:
	movl	$1,%edx
	jmp	.L899
.L898:
	movl	$0,%edx
.L899:
	cmpl	$2147483646,%ebx
	jne	.L901
.L900:
	movl	$1,%ecx
	jmp	.L902
.L901:
	movl	$0,%ecx
.L902:
	cmpl	$2147483647,%ebx
	jne	.L904
.L903:
	movl	$1,%eax
	jmp	.L905
.L904:
	movl	$0,%eax
.L905:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	movl	$0,%ebx
	movl	$0,%edi
	cmpl	$-2147483647,%ebx
	jne	.L910
.L909:
	movl	$1,%esi
	jmp	.L911
.L910:
	movl	$0,%esi
.L911:
	cmpl	$0,%ebx
	jne	.L913
.L912:
	movl	$1,%edx
	jmp	.L914
.L913:
	movl	$0,%edx
.L914:
	cmpl	$2147483646,%ebx
	jne	.L916
.L915:
	movl	$1,%ecx
	jmp	.L917
.L916:
	movl	$0,%ecx
.L917:
	cmpl	$2147483647,%ebx
	jne	.L919
.L918:
	movl	$1,%eax
	jmp	.L920
.L919:
	movl	$0,%eax
.L920:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	cmpl	$-2147483648,%ebx
	jne	.L922
.L921:
	movl	$1,%edi
	jmp	.L923
.L922:
	movl	$0,%edi
.L923:
	cmpl	$-2147483647,%ebx
	jne	.L925
.L924:
	movl	$1,%esi
	jmp	.L926
.L925:
	movl	$0,%esi
.L926:
	cmpl	$0,%ebx
	jne	.L928
.L927:
	movl	$1,%edx
	jmp	.L929
.L928:
	movl	$0,%edx
.L929:
	cmpl	$2147483646,%ebx
	jne	.L931
.L930:
	movl	$1,%ecx
	jmp	.L932
.L931:
	movl	$0,%ecx
.L932:
	cmpl	$2147483647,%ebx
	jne	.L934
.L933:
	movl	$1,%eax
	jmp	.L935
.L934:
	movl	$0,%eax
.L935:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	movl	$2147483646,%ebx
	movl	$0,%edi
	cmpl	$-2147483647,%ebx
	jne	.L940
.L939:
	movl	$1,%esi
	jmp	.L941
.L940:
	movl	$0,%esi
.L941:
	cmpl	$0,%ebx
	jne	.L943
.L942:
	movl	$1,%edx
	jmp	.L944
.L943:
	movl	$0,%edx
.L944:
	cmpl	$2147483646,%ebx
	jne	.L946
.L945:
	movl	$1,%ecx
	jmp	.L947
.L946:
	movl	$0,%ecx
.L947:
	cmpl	$2147483647,%ebx
	jne	.L949
.L948:
	movl	$1,%eax
	jmp	.L950
.L949:
	movl	$0,%eax
.L950:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	cmpl	$-2147483648,%ebx
	jne	.L952
.L951:
	movl	$1,%edi
	jmp	.L953
.L952:
	movl	$0,%edi
.L953:
	cmpl	$-2147483647,%ebx
	jne	.L955
.L954:
	movl	$1,%esi
	jmp	.L956
.L955:
	movl	$0,%esi
.L956:
	cmpl	$0,%ebx
	jne	.L958
.L957:
	movl	$1,%edx
	jmp	.L959
.L958:
	movl	$0,%edx
.L959:
	cmpl	$2147483646,%ebx
	jne	.L961
.L960:
	movl	$1,%ecx
	jmp	.L962
.L961:
	movl	$0,%ecx
.L962:
	cmpl	$2147483647,%ebx
	jne	.L964
.L963:
	movl	$1,%eax
	jmp	.L965
.L964:
	movl	$0,%eax
.L965:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	movl	$2147483647,%ebx
	movl	$0,%edi
	cmpl	$-2147483647,%ebx
	jne	.L970
.L969:
	movl	$1,%esi
	jmp	.L971
.L970:
	movl	$0,%esi
.L971:
	cmpl	$0,%ebx
	jne	.L973
.L972:
	movl	$1,%edx
	jmp	.L974
.L973:
	movl	$0,%edx
.L974:
	cmpl	$2147483646,%ebx
	jne	.L976
.L975:
	movl	$1,%ecx
	jmp	.L977
.L976:
	movl	$0,%ecx
.L977:
	cmpl	$2147483647,%ebx
	jne	.L979
.L978:
	movl	$1,%eax
	jmp	.L980
.L979:
	movl	$0,%eax
.L980:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	cmpl	$-2147483648,%ebx
	jne	.L982
.L981:
	movl	$1,%edi
	jmp	.L983
.L982:
	movl	$0,%edi
.L983:
	cmpl	$-2147483647,%ebx
	jne	.L985
.L984:
	movl	$1,%esi
	jmp	.L986
.L985:
	movl	$0,%esi
.L986:
	cmpl	$0,%ebx
	jne	.L988
.L987:
	movl	$1,%edx
	jmp	.L989
.L988:
	movl	$0,%edx
.L989:
	cmpl	$2147483646,%ebx
	jne	.L991
.L990:
	movl	$1,%ecx
	jmp	.L992
.L991:
	movl	$0,%ecx
.L992:
	cmpl	$2147483647,%ebx
	jne	.L994
.L993:
	movl	$1,%eax
	jmp	.L995
.L994:
	movl	$0,%eax
.L995:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%esi
	pushl	%edi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
.L996:
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
	pushl	_a06
	pushl	_a05
	pushl	_a04
	pushl	_a03
	pushl	_a02
	pushl	_a01
	pushl	_a00
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	pushl	_a16
	pushl	_a15
	pushl	_a14
	pushl	_a13
	pushl	_a12
	pushl	_a11
	pushl	_a10
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	pushl	_a26
	pushl	_a25
	pushl	_a24
	pushl	_a23
	pushl	_a22
	pushl	_a21
	pushl	_a20
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	pushl	_a36
	pushl	_a35
	pushl	_a34
	pushl	_a33
	pushl	_a32
	pushl	_a31
	pushl	_a30
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	pushl	_a46
	pushl	_a45
	pushl	_a44
	pushl	_a43
	pushl	_a42
	pushl	_a41
	pushl	_a40
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	pushl	_a56
	pushl	_a55
	pushl	_a54
	pushl	_a53
	pushl	_a52
	pushl	_a51
	pushl	_a50
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	pushl	_a66
	pushl	_a65
	pushl	_a64
	pushl	_a63
	pushl	_a62
	pushl	_a61
	pushl	_a60
	pushl	$_s7
	call	_printf
	leal	32(%esp),%esp
	pushl	_b03
	pushl	_b02
	pushl	_b01
	pushl	_b00
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	pushl	_b13
	pushl	_b12
	pushl	_b11
	pushl	_b10
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	pushl	_b23
	pushl	_b22
	pushl	_b21
	pushl	_b20
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	pushl	_b33
	pushl	_b32
	pushl	_b31
	pushl	_b30
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	call	_main1
	call	_main2
	call	_main3
	call	_main4
	pushl	$-9
	call	_f0
	leal	4(%esp),%esp
	pushl	$-8
	call	_f0
	leal	4(%esp),%esp
	pushl	$-1
	call	_f0
	leal	4(%esp),%esp
	pushl	$0
	call	_f0
	leal	4(%esp),%esp
	pushl	$1
	call	_f0
	leal	4(%esp),%esp
	pushl	$20
	call	_f0
	leal	4(%esp),%esp
	pushl	$21
	call	_f0
	leal	4(%esp),%esp
	pushl	$-2147483648
	call	_g0
	leal	4(%esp),%esp
	pushl	$-2147483647
	call	_g0
	leal	4(%esp),%esp
	pushl	$0
	call	_g0
	leal	4(%esp),%esp
	pushl	$2147483646
	call	_g0
	leal	4(%esp),%esp
	pushl	$2147483647
	call	_g0
	leal	4(%esp),%esp
	pushl	$-9
	call	_f1
	leal	4(%esp),%esp
	pushl	$-8
	call	_f1
	leal	4(%esp),%esp
	pushl	$-1
	call	_f1
	leal	4(%esp),%esp
	pushl	$0
	call	_f1
	leal	4(%esp),%esp
	pushl	$1
	call	_f1
	leal	4(%esp),%esp
	pushl	$20
	call	_f1
	leal	4(%esp),%esp
	pushl	$21
	call	_f1
	leal	4(%esp),%esp
	pushl	$-2147483648
	call	_g1
	leal	4(%esp),%esp
	pushl	$-2147483647
	call	_g1
	leal	4(%esp),%esp
	pushl	$0
	call	_g1
	leal	4(%esp),%esp
	pushl	$2147483646
	call	_g1
	leal	4(%esp),%esp
	pushl	$2147483647
	call	_g1
	leal	4(%esp),%esp
	pushl	$-9
	call	_f2
	leal	4(%esp),%esp
	pushl	$-8
	call	_f2
	leal	4(%esp),%esp
	pushl	$-1
	call	_f2
	leal	4(%esp),%esp
	pushl	$0
	call	_f2
	leal	4(%esp),%esp
	pushl	$1
	call	_f2
	leal	4(%esp),%esp
	pushl	$20
	call	_f2
	leal	4(%esp),%esp
	pushl	$21
	call	_f2
	leal	4(%esp),%esp
	pushl	$-2147483648
	call	_g2
	leal	4(%esp),%esp
	pushl	$-2147483647
	call	_g2
	leal	4(%esp),%esp
	pushl	$0
	call	_g2
	leal	4(%esp),%esp
	pushl	$2147483646
	call	_g2
	leal	4(%esp),%esp
	pushl	$2147483647
	call	_g2
	leal	4(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	1
	.global	_s4
_s4:
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
	.global	_s5
_s5:
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
	.global	_s7
_s7:
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
	.global	_a00
_a00:
	.long	1
	.align	4
	.global	_a01
_a01:
	.long	0
	.align	4
	.global	_a02
_a02:
	.long	0
	.align	4
	.global	_a03
_a03:
	.long	0
	.align	4
	.global	_a04
_a04:
	.long	0
	.align	4
	.global	_a05
_a05:
	.long	0
	.align	4
	.global	_a06
_a06:
	.long	0
	.align	4
	.global	_a10
_a10:
	.long	0
	.align	4
	.global	_a11
_a11:
	.long	1
	.align	4
	.global	_a12
_a12:
	.long	0
	.align	4
	.global	_a13
_a13:
	.long	0
	.align	4
	.global	_a14
_a14:
	.long	0
	.align	4
	.global	_a15
_a15:
	.long	0
	.align	4
	.global	_a16
_a16:
	.long	0
	.align	4
	.global	_a20
_a20:
	.long	0
	.align	4
	.global	_a21
_a21:
	.long	0
	.align	4
	.global	_a22
_a22:
	.long	1
	.align	4
	.global	_a23
_a23:
	.long	0
	.align	4
	.global	_a24
_a24:
	.long	0
	.align	4
	.global	_a25
_a25:
	.long	0
	.align	4
	.global	_a26
_a26:
	.long	0
	.align	4
	.global	_a30
_a30:
	.long	0
	.align	4
	.global	_a31
_a31:
	.long	0
	.align	4
	.global	_a32
_a32:
	.long	0
	.align	4
	.global	_a33
_a33:
	.long	1
	.align	4
	.global	_a34
_a34:
	.long	0
	.align	4
	.global	_a35
_a35:
	.long	0
	.align	4
	.global	_a36
_a36:
	.long	0
	.align	4
	.global	_a40
_a40:
	.long	0
	.align	4
	.global	_a41
_a41:
	.long	0
	.align	4
	.global	_a42
_a42:
	.long	0
	.align	4
	.global	_a43
_a43:
	.long	0
	.align	4
	.global	_a44
_a44:
	.long	1
	.align	4
	.global	_a45
_a45:
	.long	0
	.align	4
	.global	_a46
_a46:
	.long	0
	.align	4
	.global	_a50
_a50:
	.long	0
	.align	4
	.global	_a51
_a51:
	.long	0
	.align	4
	.global	_a52
_a52:
	.long	0
	.align	4
	.global	_a53
_a53:
	.long	0
	.align	4
	.global	_a54
_a54:
	.long	0
	.align	4
	.global	_a55
_a55:
	.long	1
	.align	4
	.global	_a56
_a56:
	.long	0
	.align	4
	.global	_a60
_a60:
	.long	0
	.align	4
	.global	_a61
_a61:
	.long	0
	.align	4
	.global	_a62
_a62:
	.long	0
	.align	4
	.global	_a63
_a63:
	.long	0
	.align	4
	.global	_a64
_a64:
	.long	0
	.align	4
	.global	_a65
_a65:
	.long	0
	.align	4
	.global	_a66
_a66:
	.long	1
	.align	4
	.global	_b00
_b00:
	.long	1
	.align	4
	.global	_b01
_b01:
	.long	0
	.align	4
	.global	_b02
_b02:
	.long	0
	.align	4
	.global	_b03
_b03:
	.long	0
	.align	4
	.global	_b10
_b10:
	.long	0
	.align	4
	.global	_b11
_b11:
	.long	1
	.align	4
	.global	_b12
_b12:
	.long	0
	.align	4
	.global	_b13
_b13:
	.long	0
	.align	4
	.global	_b20
_b20:
	.long	0
	.align	4
	.global	_b21
_b21:
	.long	0
	.align	4
	.global	_b22
_b22:
	.long	1
	.align	4
	.global	_b23
_b23:
	.long	0
	.align	4
	.global	_b30
_b30:
	.long	0
	.align	4
	.global	_b31
_b31:
	.long	0
	.align	4
	.global	_b32
_b32:
	.long	0
	.align	4
	.global	_b33
_b33:
	.long	1
