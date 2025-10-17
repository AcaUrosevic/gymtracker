import styles from "./Card.module.css";
export default function Card({ title, subtitle, children, footer }) {
  return (
    <article className={styles.card}>
      {(title || subtitle) && (
        <header className={styles.head}>
          {title && <h3>{title}</h3>}
          {subtitle && <p className={styles.sub}>{subtitle}</p>}
        </header>
      )}
      <div className={styles.body}>{children}</div>
      {footer && <footer className={styles.foot}>{footer}</footer>}
    </article>
  );
}
