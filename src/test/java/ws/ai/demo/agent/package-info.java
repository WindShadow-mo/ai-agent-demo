/**
 * <pre>
 * Tools解决的是大模型和AI应用本身交互的问题；
 * 而MCP解决的是大模型和AI应用外部交互的问题；
 * MCP client 可以理解为一种特殊的tool。
 * </pre>
 * <pre>
 * Skill三层渐进式披露：
 * 全部skill基础信息（name、description）-> 命中的skill，加载其全部内容 -> 根据skill的指引，选择加载reference文件
 * </pre>
 *
 * @author WindShadow
 * @version 2026-10-07
 */
package ws.ai.demo.agent;