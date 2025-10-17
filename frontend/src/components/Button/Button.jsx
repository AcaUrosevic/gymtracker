import styles from "./Button.module.css";
export default function Button({
  as: Tag = "button",
  children,
  variant = "primary",
  ...props
}) {
  return (
    <Tag className={`${styles.btn} ${styles[variant] || ""}`} {...props}>
      {children}
    </Tag>
  );
}
