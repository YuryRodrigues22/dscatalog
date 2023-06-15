import ContentLoader from "react-content-loader"

const CardLoader = () => (
  <ContentLoader 
    speed={2}
    width={840}
    height={384}
    viewBox="0 0 840 384"
    backgroundColor="#f3f3f3"
    foregroundColor="#ecebeb"
  >
    <rect x="7" y="11" rx="3" ry="3" width="837" height="367" />
  </ContentLoader>
)

export default CardLoader;

